package com.example.luiz.chuvinhaz.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by luiz on 10/28/15.
 */
public class Utils {


    public static String attributeToString(int att, int bad_Value)
    {
        return (att == bad_Value ? "?" : String.valueOf(att));
    }

    public static String attributeToString(boolean att, boolean bad_Value)
    {
        return (att == bad_Value ? "?" : String.valueOf(att));
    }

    public static String attributeToString(float att, float bad_Value)
    {
        return (att == bad_Value ? "?" : String.valueOf(att));
    }

    public static String attributeToString(double att, double bad_Value)
    {
        return (att == bad_Value ? "?" : String.valueOf(att));
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static boolean isUpToDate(String v2, Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;

            return v2.equals(version);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
