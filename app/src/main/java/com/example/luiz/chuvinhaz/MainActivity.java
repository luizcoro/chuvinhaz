package com.example.luiz.chuvinhaz;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.luiz.chuvinhaz.fragments.AttributesFragment;
import com.example.luiz.chuvinhaz.fragments.MyMapFragment;
import com.example.luiz.chuvinhaz.managers.IOManager;
import com.example.luiz.chuvinhaz.managers.MyLocationManager;
import com.example.luiz.chuvinhaz.managers.PreferencesManager;
import com.example.luiz.chuvinhaz.models.Inst;
import com.example.luiz.chuvinhaz.models.User;
import com.example.luiz.chuvinhaz.network.ConnectionManager;
import com.example.luiz.chuvinhaz.network.MyWebSocket;
import com.example.luiz.chuvinhaz.services.CollectorService;
import com.example.luiz.chuvinhaz.utils.Constants;
import com.example.luiz.chuvinhaz.utils.MessageHandler;
import com.example.luiz.chuvinhaz.views.adapters.NonSwipeableViewPager;
import com.example.luiz.chuvinhaz.views.dialogs.InitialDialog;

import org.java_websocket.drafts.Draft_17;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements MyMapFragment.MyMapFragmentListener, MyWebSocket.MyWebSocketListener, InitialDialog.InitialDialogListener {
    public static final int MAP_FRAGMENT = 0;
    //public static final int ATTRIBUTES_FRAGMENT = 1;
//    192.168.2.100

    private static final String server_addr = "ws://192.168.2.100:8080/chuvinhaz/websocket";

    private ChuvinhaZ mApp;

    private ViewPagerAdapter view_pager_adapter;

    private ConnectionManager connectionManager;
    private PreferencesManager preferencesManager;

    private Handler collectorHandler;

    private MyWebSocket mWebSocketClient;

    private String offline_file_path;

    private boolean first_initial_dialog = true;
    private boolean can_collect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mApp = (ChuvinhaZ) getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        NonSwipeableViewPager viewPager = (NonSwipeableViewPager) findViewById(R.id.viewpager);
        view_pager_adapter = new ViewPagerAdapter(getSupportFragmentManager());


        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        connectionManager = new ConnectionManager(getApplicationContext());
        preferencesManager = new PreferencesManager(getApplicationContext());

        collectorHandler = new Handler();

        offline_file_path = IOManager.getNextUserFilePath(new File(getFilesDir(), "uploads"));

        collectorHandler.post(new Runnable() {
            @Override
            public void run() {

                Inst instance = mApp.getCollectorServiceInstance();

                if (instance != null && can_collect) {
                    User user = mApp.getUser();
                    addReadingToWriteBuffer(System.currentTimeMillis() + "," + user.toString() + "," + instance.toString() + "\n");
                }

                collectorHandler.postDelayed(this, Constants.TWO_AND_HALF_SECONDS);
            }
        });

        setupNetworkThings();

        InitialDialog.newInstance(mApp.getUser()).show(getSupportFragmentManager(), "InitialDialog");
    }

    private void setupViewPager(ViewPager viewPager) {
        MyMapFragment mapFragment = new MyMapFragment();
        mapFragment.attachActivity(this);
        view_pager_adapter.addFragment(mapFragment, "Mapa");
        view_pager_adapter.addFragment(new AttributesFragment(), "Atributos");
        viewPager.setAdapter(view_pager_adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupNetworkThings()
    {
        if(connectionManager.isConnectingToInternet())
        {
            try {
                mWebSocketClient = new MyWebSocket(MainActivity.this, new URI(server_addr), new Draft_17());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            mWebSocketClient.connect();


            hideErrorsBar(true);

            collectorHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)) != null) {
                        ((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)).setCustomIconsEnable(true);
                    } else
                        collectorHandler.postDelayed(this, Constants.TWO_SECONDS);
                }
            });

            mApp.connectGoogleAPI();
        }
        else
        {
            hideErrorsBar(false);

            collectorHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)) != null) {
                        ((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)).setCustomIconsEnable(false);
                    } else
                        collectorHandler.postDelayed(this, Constants.TWO_SECONDS);
                }
            });

            if(mApp.isGoogleAPIConnected())
                mApp.disconnectoGoogleAPI();
        }
    }



    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.set_perfil) {
            DialogFragment initialDialog = InitialDialog.newInstance(mApp.getUser());
            initialDialog.show(getSupportFragmentManager(), "InitialDialog");
            return true;
        } else if(id == R.id.exit) {
            ActivityCompat.finishAffinity(MainActivity.this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(mApp.getCollectorServiceState() == CollectorService.state_collecting)
            startCollecting(false);

        User user = mApp.getUser();

        collectorHandler.removeCallbacksAndMessages(null);

        if(connectionManager.isConnectingToInternet() && mWebSocketClient != null && mWebSocketClient.getConnection().isOpen()) {

            String message = MessageHandler.createRemoveMessage(user);
            mWebSocketClient.send(message);
            mWebSocketClient.close();
        }

        if (isFinishing()) {
            mApp.disconnectoGoogleAPI();
            mApp.disconnectCollectorService();
        }
    }

    public void startCollecting(boolean start)
    {
        closeReadingsWriteBuffer();

        if(start)
        {
//            offline_file_path = IOManager.getNextUserFileStartingWith(getFilesDir(), "offline");

            if(mApp.getCollectorServiceState() == CollectorService.state_not_collecting)
                mApp.startCollectorService();

            can_collect = true;
        }
        else
        {
            if(mApp.getCollectorServiceState() != CollectorService.state_not_collecting)
                mApp.stopCollectorService();

            can_collect = false;
        }

    }

    public void hideErrorsBar(boolean hide) {
        View errorBar = findViewById(R.id.toolbar_sub_error_bar);
        if(errorBar != null) {
            //you can add animation here
            if(hide)
                errorBar.setVisibility(View.GONE);
            else
                errorBar.setVisibility(View.VISIBLE);
        }
    }

    private void addReadingToWriteBuffer(String reading) {
        IOManager.writeStringToFile(new File(getFilesDir(), offline_file_path), reading, true);
    }

    private void closeReadingsWriteBuffer() {
        IOManager.writeQueueToFile(new File(getFilesDir(), offline_file_path), true);
    }

    @Override
    public void onInitialDialogPositiveClick(String name, int location, int day_or_night, int air, int rainLevel, int temp, int windLevel, int humidityLevel) {

        User user = mApp.getUser();

        user.setName(name);
        user.setUser_indoor_or_outdoor(location);
        user.setUser_day_or_night(day_or_night);
        user.setUser_rain_level(rainLevel);
        user.setUser_temp(temp);
        user.setUser_wind_level(windLevel);
        user.setUser_humidity_level(humidityLevel);
        user.setUser_air(air);

        if(first_initial_dialog && user.hasLocation()) {
            ((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)).centerMapToLocation(user.getLatLng());
            first_initial_dialog = false;
        }

        startCollecting(true);

        preferencesManager.savePreferences(Constants.USER_NAME, user.getName());
    }

    @Override
    public void on_marker_click(User user_clicked) {
//        String message = MessageHandler.createFingeredMessage(mApp.getUser().getId(), user_clicked.getId());
//        mWebSocketClient.send(message);
    }

    @Override
    public void on_open() {

//        String message = MessageHandler.createAddMessage(mApp.getUser());
//        mWebSocketClient.send(message);
    }

    @Override
    public void on_connection_confirmation_message(String uuid) {
        final User me = mApp.getUser();
        me.setUUID(uuid);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)).setMe(me);
            }
        });

        collectorHandler.post(new Runnable() {
            @Override
            public void run() {

                Inst instance = mApp.getCollectorServiceInstance();

                if (instance != null) {
                    User user = mApp.getUser();
                    String message;

                    if (connectionManager.isConnectingToInternet() && mWebSocketClient != null && mWebSocketClient.getConnection().isOpen()) {
                        user.setLocation(MyLocationManager.getBestLocation(instance.getGps_location(), instance.getNetwork_location()));
                        message = MessageHandler.createUpdateMessage(user);
                        mWebSocketClient.send(message);
                    }
                }

                collectorHandler.postDelayed(this, Constants.FIVE_SECONDS);
            }
        });
    }

    @Override
    public void on_user_add_message(final String uuid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)).addMarker(uuid);
            }
        });

    }

    @Override
    public void on_user_remove_message(final String uuid) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT)).removeMarker(uuid);
            }
        });

    }

    @Override
    public void on_user_update_message(final User user) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                (((MyMapFragment) view_pager_adapter.getItem(MAP_FRAGMENT))).updateMarker(user, mApp.getCollectorServiceState() == CollectorService.state_collecting);
            }
        });
    }

    @Override
    public void on_close() {

    }

    public void refreshNetworkSetup(View v)
    {
        setupNetworkThings();
    }
}
