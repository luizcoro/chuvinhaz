package com.example.luiz.chuvinhaz.managers;

import android.content.Context;

/**
 * Created by luiz on 10/27/15.
 */
public class MyBatteryManager {

    public static double getBatteryCapacity(Context context) {
        Object mPowerProfile_ = null;

        final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";

        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS)
                    .getConstructor(Context.class).newInstance(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            return (Double) Class
                    .forName(POWER_PROFILE_CLASS)
                    .getMethod("getAveragePower", java.lang.String.class)
                    .invoke(mPowerProfile_, "battery.capacity");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1d;
    }
}
