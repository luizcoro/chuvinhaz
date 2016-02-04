package com.example.luiz.chuvinhaz.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.luiz.chuvinhaz.utils.Constants;
import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.DetectedActivity;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ActivityRecognitionIntentService extends IntentService {

    public static final String TAG = "ActivityRecognitionIntentService";


    public ActivityRecognitionIntentService() {
        // Use the TAG to name the worker thread.
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        if (ActivityRecognitionResult.hasResult(intent)) {

            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);
            Intent localIntent = new Intent(Constants.BROADCAST_ACTION);

            // Get the update
            DetectedActivity mostProbableActivity = result.getMostProbableActivity();

            // Broadcast the list of detected activities.
            localIntent.putExtra(Constants.ACTIVITY_EXTRA_CONFIDENCE, mostProbableActivity.getConfidence());
            localIntent.putExtra(Constants.ACTIVITY_EXTRA_TYPE, mostProbableActivity.getType());

            LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        }
    }
}
