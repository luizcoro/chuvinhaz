package com.example.luiz.chuvinhaz;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.luiz.chuvinhaz.managers.IOManager;
import com.example.luiz.chuvinhaz.managers.MyLocationManager;
import com.example.luiz.chuvinhaz.managers.PreferencesManager;
import com.example.luiz.chuvinhaz.models.Inst;
import com.example.luiz.chuvinhaz.models.Loc;
import com.example.luiz.chuvinhaz.network.ConnectionManager;
import com.example.luiz.chuvinhaz.network.HttpHandler;
import com.example.luiz.chuvinhaz.utils.Constants;
import com.example.luiz.chuvinhaz.utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class SplashActivity extends AppCompatActivity {


    private static final String GET_APP_VERSION_URL = "http://192.168.2.100:8080/versoes/blabla";
    private static final String SEND_FILE_URL = "http://192.168.2.100:8080/chuvinhaz/sendfile";

    private ConnectionManager connectionManager;


    private ChuvinhaZ mApp;

    private Handler handler;


    private boolean can_disconnect_services = true;
    private boolean offline_data_sent = false;
    private boolean is_up_to_date = false;
    private boolean has_connection = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        mApp = (ChuvinhaZ) getApplication();
        mApp.connectCollectorService();

        PreferencesManager preferencesManager = new PreferencesManager(getApplicationContext());
        mApp.getUser().setName(preferencesManager.loadPreferences(Constants.USER_NAME));
        mApp.getUser().setId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));

        connectionManager = new ConnectionManager(getApplicationContext());

        if(!connectionManager.isGpsOrNetworkLocationEnabled())
            buildAlertMessageNoGps(getString(R.string.alerts_gps_disabled));

        handler =  new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Inst i = mApp.getCollectorServiceInstance();

                Loc loc = MyLocationManager.getBestLocation(i.getGps_location(), i.getNetwork_location());

                if (loc != null)
                    mApp.getUser().setLocation(loc);

                handler.postDelayed(this, Constants.HALF_SECOND);
            }
        }, Constants.ONE_SECOND);

        new doInternetJobsAndStartAppTask().execute();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(can_disconnect_services) {
            if(mApp.isCollectorConnected())
                mApp.disconnectCollectorService();
        }

        handler.removeCallbacksAndMessages(null);
    }

    private void startApp() {
        can_disconnect_services = false;
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void buildAlertMessageNoGps(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.others_yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getString(R.string.others_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        ActivityCompat.finishAffinity(SplashActivity.this);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void buildAlertMessageNewerVersion(String message) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.others_yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://54.94.202.131:8080/ChuvinhaZ-Server/"));
                        startActivity(browserIntent);

                        ActivityCompat.finishAffinity(SplashActivity.this);
                    }
                })
                .setNegativeButton(getString(R.string.others_no), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        is_up_to_date = true;
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    class doInternetJobsAndStartAppTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            return connectionManager.isConnectingToInternetSafe();
        }

        @Override
        protected void onPostExecute(Boolean internet) {
            super.onPostExecute(internet);

            if (!internet) { // code if not connected
                Toast.makeText(getApplicationContext(), getString(R.string.others_no_internet_connection), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), getString(R.string.toasts_maybe_outdated), Toast.LENGTH_SHORT).show();
                has_connection = false;
            } else { // code if connected
                try {
                    new GetVersionTask().execute();
                    new SendFilesTask().execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                has_connection = true;
            }


            handler.post(new Runnable() {
                @Override
                public void run() {

                    if (mApp.getUser().hasLocation()
                            && connectionManager.isGpsOrNetworkLocationEnabled()
                            && mApp.isCollectorConnected()
                            && (!has_connection
                            || (is_up_to_date
                            && offline_data_sent))) {
                        startApp();
                    } else {
                        handler.postDelayed(this, Constants.TWO_SECONDS);
                    }

                }
            });
        }
    }

    class GetVersionTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... urls) {
            try {
                return HttpHandler.GET(GET_APP_VERSION_URL);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "";
        }

        protected void onPostExecute(String last_version) {
            Log.e("version", last_version);
            if (Utils.isUpToDate(last_version, SplashActivity.this))
                is_up_to_date = true;
            else
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        buildAlertMessageNewerVersion(getString(R.string.alerts_newer_version));
                    }
                });
        }
    }

    public class SendFilesTask extends AsyncTask<Void, Integer, Void> {

        public final int Kb = 1024;

        private ProgressDialog progressDialog;

        private File[] offline_files;

        private  int total_size = 0;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            offline_files = IOManager.getOrderedFiles(new File(getFilesDir(), "uploads"));

            if(offline_files.length > 0) {
                for (File file : offline_files)
                    total_size += file.length();

                total_size /= Kb;

                progressDialog = new ProgressDialog(SplashActivity.this);
                progressDialog.setMessage("Enviando arquivos para o servidor...");
                progressDialog.setIndeterminate(false);
                progressDialog.setMax(total_size);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progressDialog.setCancelable(false);
                progressDialog.show();
            }
        }

        @Override
        protected Void doInBackground(Void... args) {

            int progress = 0;

            for(File file : offline_files)
            {
                try {
                    byte[] data = FileUtils.readFileToByteArray(file);
                    if("ok".equals(HttpHandler.POST(SEND_FILE_URL, mApp.getUser().getId(), data)))
                    {
//                    FileUtils.forceDelete(file);
                    }

                    progress += (file.length() / Kb);
                    publishProgress(progress);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

            progressDialog.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(Void arg) {
            super.onPostExecute(arg);

            if(offline_files.length > 0)
                progressDialog.dismiss();

            offline_data_sent = true;

        }
    }
}
