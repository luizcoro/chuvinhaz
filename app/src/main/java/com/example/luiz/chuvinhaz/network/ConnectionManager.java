package com.example.luiz.chuvinhaz.network;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;

import java.io.IOException;

/**
 * Created by luiz on 6/26/15.
 */
public class ConnectionManager {

    private static String LOG_TAG = "ConnectionManager";

    private Context _context;

    public ConnectionManager(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet(){

        ConnectivityManager cm =
                (ConnectivityManager)_context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
    public boolean isConnectingToInternetSafe()
    {
        if(isWifiEnabled()) {
            Runtime runtime = Runtime.getRuntime();
            try {

                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();

                return (exitValue == 0);

            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        return false;

    }

    public boolean isWifiEnabled()
    {
        return ((WifiManager) _context.getSystemService(Context.WIFI_SERVICE)).isWifiEnabled();
    }

    public boolean isGpsOrNetworkLocationEnabled()
    {
        return (isGpsLocationEnabled() || isNetworkLocationEnabled());
    }

    public boolean isGpsLocationEnabled()
    {
        return ((LocationManager) _context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public boolean isNetworkLocationEnabled()
    {
        return ((LocationManager) _context.getSystemService(Context.LOCATION_SERVICE)).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public void isNetworkAvailable(final Handler handler, final int timeout) {
        // ask fo message '0' (not connected) or '1' (connected) on 'handler'
        // the answer must be send before before within the 'timeout' (in milliseconds)

        if(isWifiEnabled()) {
            Runtime runtime = Runtime.getRuntime();
            try {

                Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
                int     exitValue = ipProcess.waitFor();

                if(exitValue == 0)
                    handler.sendEmptyMessage(1);
                else
                    handler.sendEmptyMessage(0);

                return;

            } catch (IOException e)          { e.printStackTrace(); }
            catch (InterruptedException e) { e.printStackTrace(); }
        }

        handler.sendEmptyMessage(0);
    }
}
