package com.example.luiz.chuvinhaz.managers;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by luiz on 10/27/15.
 */
public class HardwareManager {

    public static double getScreen_size(Context context) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();

        int width;
        int height;
        int density;

        if (Build.VERSION.SDK_INT >= 17){
            //new pleasant way to get real metrics
            DisplayMetrics realMetrics = new DisplayMetrics();
            display.getRealMetrics(realMetrics);
            width = realMetrics.widthPixels;
            height = realMetrics.heightPixels;
            density = realMetrics.densityDpi;

        } else {
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            width = metrics.widthPixels;
            height = metrics.heightPixels;
            density = metrics.densityDpi;
        }

        double wi=(double)width/(double)density;
        double hi=(double)height/(double)density;
        double x = Math.pow(wi,2);
        double y = Math.pow(hi, 2);

        return Math.sqrt(x+y);
    }

    public static long[] readCpuUsage() {


        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/stat"));
            String load = br.readLine();
            String[] toks = load.split(" +");  // Split on one or more spaces
            br.close();

            long[] result = new long[4];
            result[0] = Long.parseLong(toks[1]);
            result[1] = Long.parseLong(toks[2]);
            result[2] = Long.parseLong(toks[3]);
            result[3] = Long.parseLong(toks[4]);

            return result;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static int readN_cores() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("/proc/stat"));
            br.readLine();

            int count = 0;
            while(br.readLine().startsWith("cpu"))
                count++;

            return count;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return -1;
    }


    public static double[] readLoadAvg() {

        try {
            //Construct BufferedReader from InputStreamReader
            BufferedReader br = new BufferedReader(new FileReader("/proc/loadavg"));

            String load = br.readLine();
            br.close();

            String[] toks = load.split(" +");  // Split on one or more spaces

            double[] result = new double[5];
            result[0] = Double.parseDouble(toks[0]);
            result[1] = Double.parseDouble(toks[1]);
            result[2] = Double.parseDouble(toks[2]);

            String[] now = toks[3].split("/");

            result[3] = Double.parseDouble(now[0]);
            result[4] = Double.parseDouble(now[1]);

            return result;

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    public static double getCpuUsage(long running, long total, long prev_running, long prev_total)
    {
        return ((running - prev_running)/((double)(total - prev_total))) * 100;
    }

    public static  String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }


    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }
}
