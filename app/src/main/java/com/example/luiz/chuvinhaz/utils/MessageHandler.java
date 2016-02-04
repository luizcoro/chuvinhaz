package com.example.luiz.chuvinhaz.utils;

import com.example.luiz.chuvinhaz.models.Inst;
import com.example.luiz.chuvinhaz.models.Loc;
import com.example.luiz.chuvinhaz.models.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by luiz on 01/05/15.
 */
public class MessageHandler {



    public static String createAddMessage(User user)
    {
        return "{\"action\":\"add\",\"id\":\"" + user.getId() +
                "\",\"name\":\"" + user.getName() +
                "\",\"user_temp\":" + user.getUser_temp() +
                ",\"user_rain_level\":" + user.getUser_rain_level() +
                ",\"user_humidity_level\":" + user.getUser_humidity_level() +
                ",\"user_wind_level\":" + user.getUser_wind_level() +
                ",\"indoor_or_outdoor\":" + user.getUser_indoor_or_outdoor() +
                ",\"day_or_night\":" + user.getUser_day_or_night() +
                ",\"air\":" + user.getUser_air() +
                ",\"a_rain_level\":" + user.getAlgorithm_rain_level() +
                ",\"a_rain_volume\":\"" + user.getAlgorithm_rain_volume() +
                "\",\"act\":" + user.getAct() +
                ",\"act_conf\":" + user.getConfidence_act() +
                ",\"location\":{\"lat\":\"" + user.getLocation().getLatitude() +
                "\",\"long\":\"" + user.getLocation().getLongitude() +
                "\"}}";
    }

    public static String createUpdateMessage(User user)
    {
        return "{\"action\":\"update\",\"id\":\"" + user.getId() +
                "\",\"name\":\"" + user.getName() +
                "\",\"user_temp\":" + user.getUser_temp() +
                ",\"user_rain_level\":" + user.getUser_rain_level() +
                ",\"user_humidity_level\":" + user.getUser_humidity_level() +
                ",\"user_wind_level\":" + user.getUser_wind_level() +
                ",\"indoor_or_outdoor\":" + user.getUser_indoor_or_outdoor() +
                ",\"day_or_night\":" + user.getUser_day_or_night() +
                ",\"air\":" + user.getUser_air() +
                ",\"a_rain_level\":" + user.getAlgorithm_rain_level() +
                ",\"a_rain_volume\":\"" + user.getAlgorithm_rain_volume() +
                "\",\"act\":" + user.getAct() +
                ",\"act_conf\":" + user.getConfidence_act() +
                ",\"location\":{\"lat\":\"" + user.getLocation().getLatitude() +
                "\",\"long\":\"" + user.getLocation().getLongitude() +
                "\"}}";
    }

    public static String createRemoveMessage(User user)
    {
        return "{\"action\":\"remove\",\"id\":\"" + user.getId() + "\"}";
    }

    public static String createFingeredMessage(String to, String from)
    {
        return "{\"action\":\"fingered\",\"to\":\"" + to + "\",\"from\":\"" + from + "\"}";
    }

    public static String createStartClassifyMessage(String id)
    {
        return "{\"action\":\"start_classify\",\"id\":\"" + id + "\"}";
    }

    public static String createStopClassifyMessage(String id)
    {
        return "{\"action\":\"stop_classify\",\"id\":\"" + id + "\"}";
    }

    public static String createOfflineInstance(String id, String instances)
    {
        return "{\"action\":\"offline_data\",\"id\":\"" + id + "\",\"instances\":\"" + instances +"\"}";
    }

    public static String createInstanceMessage(Inst instance, User user)
    {


        return "{\"action\":\"classify" +
                "\",\"date\":\"" + System.currentTimeMillis() +

                //user info
                "\",\"user_id\":\"" + user.getId() +
                "\",\"user_rain_level\":" + user.getUser_rain_level() +
                ",\"user_temp\":" + user.getUser_temp() +
                ",\"user_wind_level\":" + user.getUser_humidity_level() +
                ",\"user_humidity_level\":" + user.getUser_humidity_level() +
                ",\"user_location\":" + user.getUser_indoor_or_outdoor() +
                ",\"user_period\":" + user.getUser_day_or_night() +
                ",\"act\":" + user.getAct() +
                ",\"act_conf\":" + user.getConfidence_act() +

                //instance info
                ",\"device_name\":\"" + instance.getDevice().getName() +
                "\",\"phone_type\":" + instance.getDevice().getType() +
                ",\"device_screen_size\":\"" + instance.getDevice().getScreen_sizeString() +

                "\",\"cpu_avg_1m\":\"" + instance.getSoc().getCpu_avg_1mString() +
                "\",\"cpu_avg_5m\":\"" + instance.getSoc().getCpu_avg_5mString() +
                "\",\"cpu_avg_15m\":\"" + instance.getSoc().getCpu_avg_15mString() +
                "\",\"cpu_active_tasks\":" + instance.getSoc().getActive_tasks() +
                ",\"cpu_total_tasks\":" + instance.getSoc().getTotal_tasks() +
                ",\"cpu_usage_percentage\":\"" + instance.getSoc().getCpu_usage_percentageString() +
                "\",\"cpu_n_cores\":" + instance.getSoc().getN_cores() +


                ",\"network_type\":" + instance.getDevice_network().getNetwork_type() +
                ",\"lac\":" + instance.getDevice_network().getLac() +
                ",\"cid\":" + instance.getDevice_network().getCid() +
                ",\"psc\":" + instance.getDevice_network().getPsc() +
                ",\"mcc\":" + instance.getDevice_network().getMcc() +
                ",\"mnc\":" + instance.getDevice_network().getMnc() +
                ",\"gsm_sig_str\":" + instance.getDevice_network().getGsm_str() +

                ",\"gsm_neighbors_type\":" + instance.getGsm_type_array_string() +
                ",\"gsm_neighbors_lac\":" + instance.getGsm_lac_array_string() +
                ",\"gsm_neighbors_cid\":" + instance.getGsm_cid_array_string() +
                ",\"gsm_neighbors_psc\":" + instance.getGsm_psc_array_string() +
                ",\"gsm_neighbors_ss\":" + instance.getGsm_ss_array_string() +

                ",\"sat_is_fix\":" + (instance.isSat_fix() ? 1 : 0) +
                ",\"satellites_pnr\":" + instance.getSatellite_pnr_array_string() +
                ",\"satellites_ss\":" + instance.getSatellite_ss_array_string() +

                ",\"wifis_freq\":" + instance.getWifi_frequency_array_string() +
                ",\"wifis_ss\":" + instance.getWifi_ss_array_string() +

                ",\"gps_accuracy\":\"" + instance.getGps_accuracyString() +
                "\",\"gps_lat\":\"" + instance.getGps_latString() +
                "\",\"gps_long\":\"" + instance.getGps_lngString() +
                "\",\"gps_alt\":\"" + instance.getGps_altitudeString() +
                "\",\"gps_speed\":\"" + instance.getGps_speedString() +

                "\",\"network_accuracy\":\"" + instance.getNetwork_accuracyString() +
                "\",\"network_lat\":\"" + instance.getNetwork_latString() +
                "\",\"network_long\":\"" + instance.getNetwork_lngString() +
                "\",\"network_alt\":\"" + instance.getNetwork_altitudeString() +
                "\",\"network_speed\":\"" + instance.getNetwork_speedString() +

                "\",\"battery_health\":" + instance.getBattery().getHealth() +
                ",\"battery_plugged\":" + instance.getBattery().getPlugged() +
                ",\"battery_status\":" + instance.getBattery().getStatus() +
                ",\"battery_percentage\":\"" + instance.getBattery().getPercentageString() +
                "\",\"battery_capacity\":\"" + instance.getBattery().getCapacity() +
                "\",\"batteryTemperature\":\"" + instance.getBattery().getTempString() +
                "\",\"battery_voltage\":" + instance.getBattery().getVoltage() +

                ",\"ambient_temp\":\"" + instance.getSensors().getAmbient_tempString() +
                "\",\"light\":\"" + instance.getSensors().getLightString() +
                "\",\"pressure\":\"" + instance.getSensors().getPressureString() +
                "\",\"relative_humidity\":\"" + instance.getSensors().getRelative_humidityString() +
                "\",\"proximity\":\"" + instance.getSensors().getProximityString() +
                "\",\"acc_x\":\"" + instance.getSensors().getAccelerometerXString() +
                "\",\"acc_y\":\"" + instance.getSensors().getAccelerometerYString() +
                "\",\"acc_z\":\"" + instance.getSensors().getAccelerometerZString() +

                "\",\"ac_ambient_temp\":\"" + instance.getSensors().getAc_ambient_tempString() +
                "\",\"ac_light\":\"" + instance.getSensors().getAc_lightString() +
                "\",\"ac_pressure\":\"" + instance.getSensors().getAc_pressureString() +
                "\",\"ac_relative_humidity\":\"" + instance.getSensors().getAc_relative_humidityString() +
                "\",\"ac_proximity\":\"" + instance.getSensors().getAc_proximityString() +
                "\",\"ac_accelerometer\":\"" + instance.getSensors().getAc_accelerometerString() +
                "\"}";
    }

    public static User messageToUser(JSONObject message) throws JSONException {

        User user = new User();
        user.setUUID(message.getString("uuid"));

        if("remove".equals(message.getString("action")))
            return user;

        user.setId(message.getString("id"));
        user.setName(message.getString("name"));
        user.setUser_rain_level(message.getInt("user_rain_level"));
        user.setUser_temp(message.getInt("user_temp"));
        user.setUser_humidity_level(message.getInt("user_humidity_level"));
        user.setUser_wind_level(message.getInt("user_wind_level"));
        user.setUser_indoor_or_outdoor(message.getInt("indoor_or_outdoor"));
        user.setUser_day_or_night(message.getInt("day_or_night"));
        user.setAlgorithm_rain_level(message.getInt("a_rain_level"));
        user.setAlgorithm_rain_volume(Double.parseDouble(message.getString("a_rain_volume")));
        user.setAct(message.getInt("act"));
        user.setConfidence_act(message.getInt("act_conf"));

        JSONObject location = message.getJSONObject("location");

        //loc parameters - (acc, lat, lng , alt, speed)
        user.setLocation( new Loc(-1.f, Double.parseDouble(location.getString("lat")),Double.parseDouble(location.getString("long")), -1.d, -1.f));
        return user;
    }


}
