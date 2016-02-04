//package com.example.luiz.chuvinhaz.activities;
//
//import android.content.BroadcastReceiver;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.location.LocationManager;
//import android.net.ConnectivityManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.provider.Settings;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.Toast;
//
//import com.example.luiz.chuvinhaz.R;
//import com.example.luiz.chuvinhaz.ChuvinhaZ;
//import com.example.luiz.chuvinhaz.views.dialogs.InitialDialog;
//import com.example.luiz.chuvinhaz.handlers.MapHandler;
//import com.example.luiz.chuvinhaz.utils.MessageHandler;
//import com.example.luiz.chuvinhaz.models.Inst;
//import com.example.luiz.chuvinhaz.models.User;
//import com.example.luiz.chuvinhaz.services.CollectorService;
//import com.example.luiz.chuvinhaz.network.InternetChangedReceiver;
//import com.example.luiz.chuvinhaz.network.ConnectionManager;
//import com.example.luiz.chuvinhaz.utils.Constants;
//import com.example.luiz.chuvinhaz.managers.IOManager;
//import com.example.luiz.chuvinhaz.managers.PreferencesManager;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft_17;
//import org.java_websocket.handshake.ServerHandshake;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.io.UnsupportedEncodingException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.util.LinkedList;
//import java.util.Queue;
//
//
//public class MainActivity extends AppCompatActivity implements
//        OnMapReadyCallback, InitialDialog.InitialDialogListener, InternetReactor.InternetReactorListener/*, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> */{
//
//    public static final String TAG = "MainActivity";
//
//    private Toolbar _toobar;
//
//    private static final int MAX_BUFFER = 5;
//
//    private ChuvinhaZ mApp;
//
//    private ConnectionManager connectionManager;
//    private InternetReactor internetReactor;
//
//    PreferencesManager preferencesManager;
//
//    private MessageHandler mMessageHandler;
//    private WebSocketClient mWebSocketClient;
//
//    private BroadcastReceiver connectivityBroadcastReceiver;
//
//    private Handler collectorHandler;
//    private Runnable getDataFromCollector;
//
//    private SupportMapFragment mapFragment;
//
//    private Queue<String> queue;
//
//    private boolean camera_moved = false;
//    private boolean verifying_gps = false;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
////        _toobar = (Toolbar) findViewById(R.id.toolbar_main);
////        _toobar.setTitle(getResources().getString(R.string.app_name));
////        _toobar.setLogo(getResources().getDrawable(R.mipmap.ic_launcher));
////
////        setSupportActionBar(_toobar);
//
//
//        mApp = (ChuvinhaZ) getApplication();
//
//
//
//        connectionManager = new ConnectionManager(getApplicationContext());
//        preferencesManager = new PreferencesManager(getApplicationContext());
//        internetReactor = new InternetReactor(getApplicationContext(), this);
//
//        mMessageHandler = new MessageHandler();
//
//        getDataFromCollector = new CollectorServiceRunnable();
//        collectorHandler = new Handler();
//
//        queue = new LinkedList<String>();
//
//        if(savedInstanceState == null) {
//
//
//            if (!connectionManager.isGpsEnabled()) {
//                buildAlertMessageNoGps("Your GPS seems to be disabled, do you want to enable it?");
//            }
//            else {
//                startThingsJustOneTime();
//            }
//
//        }
//
//        installInternetReactor();
//
//        if(mapFragment == null)
//            setGoogleMaps();
//
//
//        collectorHandler.post(getDataFromCollector);
//    }
//
//
//    @Override
//    public void onInitialDialogPositiveClick(String name, int location, int day_or_night, int rainLevel, int temp, int windLevel, int humidityLevel) {
//
//        User user = mApp.getUser();
//
//        user.setName(name);
//        user.setUser_indoor_or_outdoor(location);
//        user.setUser_day_or_night(day_or_night);
//        user.setUser_rain_level(rainLevel);
//        user.setUser_temp(temp);
//        user.setUser_wind_level(windLevel);
//        user.setUser_humidity_level(humidityLevel);
//
//        if(user.hasLocation()) {
//            if (!camera_moved) {
//                mMapHandler.centerMapToLocation(user.getLatLng());
//                camera_moved = true;
//            }
//        }
//        preferencesManager.savePreferences(Constants.USER_NAME, user.getName());
//    }
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//        map.setMyLocationEnabled(true);
//    }
//
//    public void setGoogleMaps()
//    {
//        /*mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(MainActivity.this);
//        GoogleMap map = mapFragment.getMap();
//        map.setInfoWindowAdapter(new GoogleMapsAdapter(getApplicationContext()));
//        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                String message = mMessageHandler.createFingeredMessage(mApp.getUser().getId(), mMapHandler.getUserByMarker(marker));
//                sendWebsocketMessage(message);
//
//                return false;
//            }
//        });
//
//        mMapHandler = new MapHandler(map);*/
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        if(verifying_gps)
//        {
//            if(!connectionManager.isGpsEnabled())
//            {
//                buildAlertMessageNoGps("Your GPS keeps disabled, do you want to enable it?");
//            }
//
//            verifying_gps = false;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//
//        User user = mApp.getUser();
//
//        collectorHandler.removeCallbacks(getDataFromCollector);
//
//        if(user.hasLocation() && mWebSocketClient != null && mWebSocketClient.getConnection().isOpen()) {
//            String message = mMessageHandler.createRemoveMessage(user);
//            sendWebsocketMessage(message);
//            mWebSocketClient.close();
//        }
//        disconnectWebsocketClient();
//        uninstallInternetReactor();
//
//        closeReadingsWriteBuffer("readings");
//
//        if (isFinishing()) {
//            mApp.disconnectCollectorService();
//            mApp.disconnectoGoogleAPI();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//
//        if(mApp != null && (mApp.getCollectorServiceState() == CollectorService.state_preparing_to_collecting || mApp.getCollectorServiceState() == CollectorService.state_collecting)) {
//            menu.findItem(R.id.toggle_collecting).setIcon(android.R.drawable.ic_media_pause);
//        }
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if(id == R.id.attributes_activity) {
//            Intent intent = new Intent(this, AttributesActivity.class);
//            startActivity(intent);
//            return true;
//        }else if(id == R.id.set_perfil) {
//            DialogFragment initialDialog = InitialDialog.newInstance(mApp.getUser());
//            initialDialog.show(getSupportFragmentManager(), "InitialDialog");;
//            return true;
//        } else if(id == R.id.toggle_collecting) {
//            int state = mApp.getCollectorServiceState();
//            if(state == CollectorService.state_not_collecting) {
//                item.setIcon(android.R.drawable.ic_media_pause);
//                mApp.startCollectorService();
//
//            } else {
//                item.setIcon(android.R.drawable.ic_media_play);
//                mApp.stopCollectorService();
//
//            }
//            return true;
//        } else if(id == R.id.exit) {
//            ActivityCompat.finishAffinity(MainActivity.this);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//    private void connectWebSocket() throws InterruptedException {
//        URI uri;
//
//
//        try {
//            uri = new URI("ws://54.94.202.131:8080/ChuvinhaZ-Server/actions");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return;
//        }
//
//        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
//            @Override
//            public void onOpen(ServerHandshake serverHandshake) {
//                Log.i("Websocket", "Opened");
//
//
//
//
//                //wait until all services are connected
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
//
//                        try {
//                            while (!mApp.isCollectorConnected()) {
//                                Thread.sleep(1000);
//                            }
//
//                            User user = mApp.getUser();
//
//                            if(!user.hasLocation())
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        Toast.makeText(getApplicationContext(), "Checking user location", Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//                            while(true) {
//
//                                user = mApp.getUser();
//
//                                if(user.hasLocation())
//                                    break;
//
//                                Thread.sleep(1000);
//                            }
//
//                            if (!camera_moved) {
//                                final User user2 = user;
//                                runOnUiThread(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        mMapHandler.centerMapToLocation(user2.getLatLng());
//                                    }
//                                });
//
//                                camera_moved = true;
//                            }
//
//
//
//                            String message = mMessageHandler.createAddMessage(mApp.getUser());
//                            Log.i("Websocket", message);
//                            sendWebsocketMessage(message);
//
//                            byte[] data = IOManager.readByteArrayFromFile(new File(getFilesDir(), "readings"));
//
//                            if(data != null) {
//
//
//                                try {
//                                    String decoded = new String(data, "UTF-8");
//                                    Log.e("offline_data", decoded);
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//
//                                sendWebsocketMessage(data);
//                                deleteFile(new File(getFilesDir(), "readings"));
//                            }
//
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();
//            }
//
//            @Override
//            public void onMessage(String s) {
//                try {
//                    JSONObject message = new JSONObject(s);
//
//                    if("add".equals(message.getString("action"))) {
//                        final User new_user = mMessageHandler.messageToUser(message);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mMapHandler.addMarker(new_user);
//                            }
//                        });
//
//                    } else if("update".equals(message.getString("action"))) {
//                        final User user_to_update = mMessageHandler.messageToUser(message);
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//
//                                mMapHandler.updateMarker(user_to_update, mApp.getCollectorServiceState() == CollectorService.state_collecting);
//                            }
//                        });
//
//                    } else if("remove".equals(message.getString("action"))) {
//
//                        final String user_id = message.getString("id");
//
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                mMapHandler.removeMarker(user_id);
//                            }
//                        });
//
////                    } else if("fingeredby".equals(message.getString("action"))) {
////                        final String user_id = message.getString("id");
////
////                        runOnUiThread(new Runnable() {
////                            @Override
////                            public void run() {
////                                String user_name = mMapHandler.getUserNameById(user_id);
////                                Toast.makeText( getApplicationContext(), "You are fingered by: " + user_name, Toast.LENGTH_SHORT).show();
////                            }
////                        });
//
//
//                    } else if("result".equals(message.getString("action"))) {
//                        User user = mApp.getUser();
//                        user.setAlgorithm_rain_level(message.getInt("class"));
//                        user.setAlgorithm_rain_volume(Double.parseDouble(message.getString("volume")));
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onClose(int i, String s, boolean b) {
//                Log.i("Websocket", "Closed " + s);
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                e.printStackTrace();
//            }
//        };
//
//        mWebSocketClient.connect();
//    }
//
//    public class CollectorServiceRunnable implements Runnable
//    {
//
//        @Override
//        public void run() {
//            Inst instance = mApp.getCollectorServiceInstance();
//
//            if(instance != null && instance.getLocations().locationSet()) {
//                User user = mApp.getUser();
//                String message;
//                if( mApp.getCollectorServiceState() == CollectorService.state_collecting) {
//
//                    if(connectionManager.isConnectingToInternet()) {
//                        message = MessageHandler.createInstanceMessage(instance, user);
//                        mWebSocketClient.send(message);
//
//                        if(LocationManager.GPS_PROVIDER.equals(instance.getLocations().getBestProvider()))
//                        user.setLocation(instance.getLocations().getGps_lat(), instance.getLocations().getGps_lng());
//                        else
//                        user.setLocation(instance.getLocations().getNetwork_lat(), instance.getLocations().getNetwork_lng());
//
//                        message = MessageHandler.createUpdateMessage(user);
//
//                        //Log.i("teste", message);
//                        mWebSocketClient.send(message);
//                    }
//                    else
//                        addReadingToWriteBuffer("readings", System.currentTimeMillis() + "," +  user.toString() + "," + instance.toString() + "\n");
//                }
//            }
//
//            collectorHandler.postDelayed(this, 5000);
//
//        }
//    }
//
//    private void addReadingToWriteBuffer(String filename, String reading)
//    {
//        queue.add(reading);
//
//        if(queue.size() == MAX_BUFFER) {
//            StringBuilder builder = new StringBuilder();
//
//            while(!queue.isEmpty())
//                builder.append(queue.remove());
//
//            IOManager.writeStringToFile(new File(getFilesDir(), "readings"), builder.toString(), true);
//        }
//    }
//
//    private void closeReadingsWriteBuffer(String filename)
//    {
//
//        if(queue.isEmpty())
//            return;
//
//        StringBuilder builder = new StringBuilder();
//
//        while(!queue.isEmpty())
//            builder.append(queue.remove());
//
//        IOManager.writeStringToFile(new File(getFilesDir(), "readings"), builder.toString(), true);
//    }
//
//    private void deleteFile(File file)
//    {
//        file.delete();
//    }
//
//    private void installInternetReactor() {
//
//        final IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//        registerReceiver(internetReactor, intentFilter);
//
//    }
//
//    private void uninstallInternetReactor()
//    {
//        unregisterReceiver(internetReactor);
//    }
//
//    @Override
//    public void onNetworkStateTrigger(boolean got_network) {
//        Log.e("listener", "network: " + got_network);
//        if(got_network)
//        {
//            //Toast.makeText(this, "Internet is up.", Toast.LENGTH_LONG).show();
//            mWebSocketClient.
//            try {
//                connectWebSocket();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            if(!mApp.isGoogleAPIConnected())
//                mApp.connectGoogleAPI();
//        }
//        else
//        {
//            //Toast.makeText(this, "Internet is down.", Toast.LENGTH_LONG).show();
//
//            mWebSocketClient.close();
//
//            if(mApp.isGoogleAPIConnected())
//                mApp.disconnectoGoogleAPI();
//        }
//    }
//    private void buildAlertMessageNoGps(String message) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage(message)
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//
//                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                        startThingsJustOneTime();
//
//                        verifying_gps = true;
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//
//                        ActivityCompat.finishAffinity(MainActivity.this);
//
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();
//    }
//
//    private void startThingsJustOneTime()
//    {
//        mApp.connectCollectorService();
//
//        mApp.getUser().setName(preferencesManager.loadPreferences(Constants.USER_NAME));
//        mApp.getUser().setId(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
//
//        DialogFragment initialDialog = InitialDialog.newInstance(mApp.getUser());
//        initialDialog.show(getSupportFragmentManager(), "InitialDialog");
//    }
//}
