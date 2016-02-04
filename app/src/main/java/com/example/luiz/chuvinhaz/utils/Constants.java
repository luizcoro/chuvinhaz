package com.example.luiz.chuvinhaz.utils;

/**
 * Created by luiz on 5/24/15.
 */

import android.content.res.Resources;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;

import com.example.luiz.chuvinhaz.R;
import com.google.android.gms.location.DetectedActivity;

/**
 * Constants used in this sample.
 */
public final class Constants {

    public static Resources resources;

    //Attributes
    public static final int
        DEVICE_NAME = 0,
        PHONE_TYPE = 1,
        DEVICE_SCREEN_SIZE = 2,

        ACT = 3,
        ACT_CONFIDENCE = 4,

        CPU_AVG_1M = 5,
        CPU_AVG_5M = 6,
        CPU_AVG_15M = 7,
        CPU_ACTIVE_TASKS = 8,
        CPU_TOTAL_TASKS = 9,
        CPU_USAGE_PERCENTAGE = 10,
        CPU_N_CORES = 11,

        NETWORK_TYPE = 12,
        CID = 13,
        LAC = 14,
        PSC = 15,
        MCC = 16,
        MNC = 17,
        GSM_STR = 18,

        GSM_TYPE_ARRAY = 19,
        GSM_CID_ARRAY = 20,
        GSM_LAC_ARRAY = 21,
        GSM_PSC_ARRAY = 22,
        GSM_SS_ARRAY = 23,

        SAT_FIX = 24,
        SAT_PNR_ARRAY = 25,
        SAT_SS_ARRAY = 26,

        WIFI_FREQ_ARRAY = 27,
        WIFI_SS_ARRAY = 28,

        GPS_ACCURACY = 29,
        GPS_LAT = 30,
        GPS_LNG = 31,
        GPS_ALTITUDE = 32,
        GPS_SPEED = 33,

        NETWORK_ACCURACY = 34,
        NETWORK_LAT = 35,
        NETWORK_LNG = 36,
        NETWORK_ALTITUDE = 37,
        NETWORK_SPEED = 38,

        BATTERY_HEALTH = 39,
        BATTERY_PLUGGED = 40,
        BATTERY_STATUS = 41,
        BATTERY_PERCENTAGE = 42,
        BATTERY_CAPACITY = 43,
        BATTERY_TEMP = 44,
        BATTERY_VOLTAGE = 45,

        AMBIENT_TEMP = 46,
        LIGHT = 47,
        PRESSURE = 48,
        RELATIVE_HUMIDITY = 49,
        PROXIMITY = 50,
        ACCELEROMETER_ARRAY = 51,
        AMBIENT_TEMP_ACCURACY = 52,
        LIGHT_ACCURACY = 53,
        PRESSURE_ACCURACY = 54,
        RELATIVE_HUMIDITY_ACCURACY = 55,
        PROXIMITY_ACCURACY = 56,
        ACCELEROMETER_ACCURACY = 57;



    //File
    public static final int MAX_BUFFER_QUEUE = 5;


    //Times
    public static final int HALF_SECOND = 500;
    public static final int ONE_SECOND = 1000;
    public static final int TWO_SECONDS = 2000;
    public static final int TWO_AND_HALF_SECONDS = 2500;
    public static final int FIVE_SECONDS = 5000;
    public static final int TEN_SECONDS = 10000;
    public static final int FIFTH_SECONDS = 15000;
    public static final long ZERO_SECONDS = 0;


    //Activities
    public static final String MAIN_ACTIVITY = "MainActivity";

    //Packages
    public static final String PACKAGE_NAME = "com.google.android.gms.location.activityrecognition";
    public static final String BROADCAST_ACTION = PACKAGE_NAME + ".BROADCAST_ACTION";
    public static final String ACTIVITY_EXTRA_CONFIDENCE = PACKAGE_NAME + ".ACTIVITY_EXTRA_CONFIDENCE";
    public static final String ACTIVITY_EXTRA_TYPE = PACKAGE_NAME + ".ACTIVITY_EXTRA_TYPE";
    public static final String SHARED_PREFERENCES_NAME = PACKAGE_NAME + ".SHARED_PREFERENCES";
    public static final String ACTIVITY_UPDATES_REQUESTED_KEY = PACKAGE_NAME + ".ACTIVITY_UPDATES_REQUESTED";
    public static final String DETECTED_ACTIVITIES = PACKAGE_NAME + ".DETECTED_ACTIVITIES";


    //Shared preerences
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";



    public static String getActivityString(int detectedActivityType) {
        switch(detectedActivityType) {
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.options_recognition_in_vehicle);
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.options_recognition_on_bicycle);
            case DetectedActivity.ON_FOOT:
                return resources.getString(R.string.options_recognition_on_foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.options_recognition_running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.options_recognition_still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.options_recognition_tilting);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.options_recognition_walking);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getNeighborNetworkType(int type) {
        switch (type) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return resources.getString(R.string.options_neighbor_network_gsm);
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return resources.getString(R.string.options_neighbor_network_umts);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getNetworkTypeString(int type) {
        switch(type) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return resources.getString(R.string.options_network_1xrtt);
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return resources.getString(R.string.options_network_cdma);
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return resources.getString(R.string.options_network_edge);
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return resources.getString(R.string.options_network_ehrpd);
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return resources.getString(R.string.options_network_evdo_0);
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return resources.getString(R.string.options_network_evdo_a);
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return resources.getString(R.string.options_network_evdo_b);
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return resources.getString(R.string.options_network_gprs);
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return resources.getString(R.string.options_network_hsdpa);
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return resources.getString(R.string.options_network_hspa);
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return resources.getString(R.string.options_network_hspa_plus);
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return resources.getString(R.string.options_network_hsupa);
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return resources.getString(R.string.options_network_iden);
            case TelephonyManager.NETWORK_TYPE_LTE:
                return resources.getString(R.string.options_network_lte);
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return resources.getString(R.string.options_network_umts);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getPhoneTypeString(int type) {
        switch(type) {
            case TelephonyManager.PHONE_TYPE_CDMA:
                return resources.getString(R.string.options_phone_cdma);
            case TelephonyManager.PHONE_TYPE_GSM:
                return resources.getString(R.string.options_phone_gsm);
            case TelephonyManager.PHONE_TYPE_NONE:
                return resources.getString(R.string.options_phone_none);
            case TelephonyManager.PHONE_TYPE_SIP:
                return resources.getString(R.string.options_phone_sip);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getBatteryHealth(int heath) {
        switch(heath) {
            case BatteryManager.BATTERY_HEALTH_COLD:
                return resources.getString(R.string.options_battery_cold);
            case BatteryManager.BATTERY_HEALTH_DEAD:
                return resources.getString(R.string.options_battery_dead);
            case BatteryManager.BATTERY_HEALTH_GOOD:
                return resources.getString(R.string.options_battery_good);
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                return resources.getString(R.string.options_battery_over_voltage);
            case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                return resources.getString(R.string.options_battery_overheat);
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                return resources.getString(R.string.options_battery_unspecified_failure);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getBatteryPlugged(int plugged) {
        switch(plugged) {
            case BatteryManager.BATTERY_PLUGGED_AC:
                return resources.getString(R.string.options_battery_plugged_ac);
            case BatteryManager.BATTERY_PLUGGED_USB:
                return resources.getString(R.string.options_battery_plugged_usb);
            case BatteryManager.BATTERY_PLUGGED_WIRELESS:
                return resources.getString(R.string.options_battery_plugged_wireless);
            default:
                return resources.getString(R.string.options_battery_plugged_not_plugged);
        }
    }

    public static String getBatteryStatus(int status) {
        switch(status) {
            case BatteryManager.BATTERY_STATUS_CHARGING:
                return resources.getString(R.string.options_battery_status_charging);
            case BatteryManager.BATTERY_STATUS_DISCHARGING:
                return resources.getString(R.string.options_battery_status_discharging);
            case BatteryManager.BATTERY_STATUS_FULL:
                return resources.getString(R.string.options_battery_status_full);
            case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                return resources.getString(R.string.options_battery_status_not_charging);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getLocationString(int location_id)
    {
        switch(location_id)
        {
            case 0:
                return resources.getString(R.string.options_location_indoors);
            case 1:
                return resources.getString(R.string.options_location_outdoors);
            case 2:
                return resources.getString(R.string.options_location_car);
            default:
                return "?";
        }
    }

    public static String getDayOrNightString(int id)
    {
        switch(id)
        {
            case 0:
                return resources.getString(R.string.options_period_day);
            case 1:
                return resources.getString(R.string.options_period_night);
            default:
                return "?";
        }
    }

    public static String getWeatherString(int weather_id)
    {
        switch(weather_id)
        {
            case 0:
                return resources.getString(R.string.options_weather_clear);
            case 1:
                return resources.getString(R.string.options_weather_mostly_cloud);
            case 2:
                return resources.getString(R.string.options_weather_cloudy);
            case 3:
                return resources.getString(R.string.options_weather_drizzle);
            case 4:
                return resources.getString(R.string.options_weather_showers);
            case 5:
                return resources.getString(R.string.options_weather_thunderstorms);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }


    public static String getHumidityString(int humidity_id)
    {
        switch(humidity_id)
        {
            case 0:
                return resources.getString(R.string.options_humidity_dry);
            case 1:
                return resources.getString(R.string.options_humidity_little_wet);
            case 2:
                return resources.getString(R.string.options_humidity_wet);
            case 3:
                return resources.getString(R.string.options_humidity_very_wet);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getWindString(int wind_id)
    {
        switch(wind_id)
        {
            case 0:
                return resources.getString(R.string.options_wind_no_wind);
            case 1:
                return resources.getString(R.string.options_wind_weak_wind);
            case 2:
                return resources.getString(R.string.options_wind_strong_wind);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static String getAirString(int air)
    {
        switch(air) {
            case 0:
                return resources.getString(R.string.options_air_off);
            case 1:
                return resources.getString(R.string.options_air_on);
            default:
                return resources.getString(R.string.options_general_unknown);
        }
    }

    public static int getWeatherImageId(int progress)
    {
        switch(progress) {
            case 0: return R.id.clear;
            case 1: return R.id.mostly_cloud;
            case 2: return R.id.cloudy;
            case 3: return R.id.drizzle;
            case 4: return R.id.showers;
            case 5: return R.id.thunderstorms;
            default: return 9999;
        }

    }

    public static int getHumidityImageId(int progress)
    {
        switch(progress) {
            case 0: return R.id.drop_1;
            case 1: return R.id.drop_3;
            case 2: return R.id.drop_10;
            default: return 9999;
        }

    }

    public static int getWindImageId(int progress)
    {
        switch(progress) {
            case 0: return R.id.weak_wind;
            case 1: return R.id.moderate_wind;
            case 2: return R.id.strong_wind;
            default: return 9999;
        }
    }




}
