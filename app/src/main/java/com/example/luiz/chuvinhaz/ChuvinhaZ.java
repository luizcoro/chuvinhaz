package com.example.luiz.chuvinhaz;

import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.luiz.chuvinhaz.models.Inst;
import com.example.luiz.chuvinhaz.models.User;
import com.example.luiz.chuvinhaz.services.ActivityRecognitionIntentService;
import com.example.luiz.chuvinhaz.services.CollectorConnection;
import com.example.luiz.chuvinhaz.services.CollectorService;
import com.example.luiz.chuvinhaz.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.ActivityRecognition;


public class ChuvinhaZ extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ResultCallback<Status> {

    private String TAG = ".ChuvinhaZ";

    private User user;

    private CollectorConnection collectorConnection;
    private boolean collector_connected = false;

    protected ActivityDetectionBroadcastReceiver mActivityBroadcastReceiver;
    protected GoogleApiClient mGoogleApiClient;
    private PendingIntent mActivityDetectionPendingIntent;



    @Override
    public void onCreate() {
        super.onCreate();

        Constants.resources = getResources();

        user = new User();

        collectorConnection = new CollectorConnection();

        mActivityBroadcastReceiver = new ActivityDetectionBroadcastReceiver();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(ActivityRecognition.API)
                .build();
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "Connected to GoogleApiClient");
        requestActivityUpdatesRecognition();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Refer to the javadoc for ConnectionResult to see what error codes might be returned in
        // onConnectionFailed.
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }

    @Override
    public void onConnectionSuspended(int cause) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection.
        Log.i(TAG, "Connection suspended");

        mGoogleApiClient.connect();
    }

    @Override
    public void onResult(Status status) {
        if(status.isSuccess())
            Log.i(TAG, "Connection ok");
        else
            Log.i(TAG, "Connection not ok");

    }

    public void connectGoogleAPI()
    {
        mGoogleApiClient.connect();
        LocalBroadcastManager.getInstance(this).registerReceiver(mActivityBroadcastReceiver, new IntentFilter(Constants.BROADCAST_ACTION));
    }

    public void disconnectoGoogleAPI()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mActivityBroadcastReceiver);
        removeActivityUpdatesRecognition();
        mGoogleApiClient.disconnect();
    }


    public void connectCollectorService() {
        bindService(new Intent(this, CollectorService.class), collectorConnection, Context.BIND_AUTO_CREATE | Context.BIND_IMPORTANT);
        collector_connected = true;
    }


    public void disconnectCollectorService() {
        if(collectorConnection.isCollectorBound() && collector_connected) {
            stopService(new Intent(new Intent(this, CollectorService.class)));
            unbindService(collectorConnection);

            collector_connected = false;
        }
    }

    public void startCollectorService() {
        collectorConnection.getCollectorService().startCollecting();
    }

    public void stopCollectorService() {
        collectorConnection.getCollectorService().stopCollecting();
    }

    public int getCollectorServiceState(){
        if(collectorConnection.getCollectorService() == null)
            return -1;

        return collectorConnection.getCollectorService().getState();
    }

    public User getUser()
    {
        return user;
    }

    public Inst getCollectorServiceInstance()
    {
        if (collectorConnection.getCollectorService() == null)
            return null;

        return collectorConnection.getCollectorService().getInstance();
    }

    public class ActivityDetectionBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            user.setConfidence_act(intent.getIntExtra(Constants.ACTIVITY_EXTRA_CONFIDENCE, 0));
            user.setAct(intent.getIntExtra(Constants.ACTIVITY_EXTRA_TYPE, -1));
        }
    }

    public void requestActivityUpdatesRecognition()
    {
        if (mGoogleApiClient.isConnected()) {

            ActivityRecognition.ActivityRecognitionApi.requestActivityUpdates(
                    mGoogleApiClient,
                    Constants.ZERO_SECONDS,
                    getActivityDetectionPendingIntent()
            ).setResultCallback(this);

        }
    }

    private void removeActivityUpdatesRecognition() {

        if (mGoogleApiClient.isConnected()) {
            // Remove all activity updates for the PendingIntent that was used to request activity
            // updates.
            ActivityRecognition.ActivityRecognitionApi.removeActivityUpdates(
                    mGoogleApiClient,
                    getActivityDetectionPendingIntent()
            ).setResultCallback(this);

        }
    }

    private PendingIntent getActivityDetectionPendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mActivityDetectionPendingIntent != null) {
            return mActivityDetectionPendingIntent;
        }
        Intent intent = new Intent(this, ActivityRecognitionIntentService.class);

        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public boolean isCollectorConnected()
    {
        return collectorConnection.isCollectorBound();
    }

    public boolean isGoogleAPIConnected()
    {
        return mGoogleApiClient.isConnected();
    }

}
