package com.example.luiz.chuvinhaz.services;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Created by luiz on 6/23/15.
 */

public class CollectorConnection implements ServiceConnection {
    private CollectorService collectorService;
    private boolean collectorBound;

    public CollectorConnection() {
        collectorService = null;
        this.collectorBound = false;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        CollectorService.LocalBinder binder = (CollectorService.LocalBinder) service;
        collectorService = binder.getService();
        collectorBound = true;

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        collectorService = null;
        collectorBound = false;
    }

    public CollectorService getCollectorService()
    {
        return collectorService;
    }

    public boolean isCollectorBound() {
        return collectorBound;
    }
}