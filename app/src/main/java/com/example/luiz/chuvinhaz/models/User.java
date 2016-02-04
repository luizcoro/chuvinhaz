package com.example.luiz.chuvinhaz.models;

import com.example.luiz.chuvinhaz.utils.Constants;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by luiz on 01/05/15.
 */
public class User {
    private String id;
    private String uuid;
    private String name;
    private int user_rain_level;
    private int user_temp;
    private int user_wind_level;
    private int user_humidity_level;
    private int user_air;

    private int user_indoor_or_outdoor;
    private int user_day_or_night;
    private int act;
    private int confidence_act;

    private Loc location;

    private int algorithm_rain_level;
    private double algorithm_rain_volume;
    private int algorithm_indoor_or_outdoor;


    public User(){

        name = "";
        user_rain_level = -1;
        user_temp = -1;
        user_wind_level = -1;
        user_humidity_level = -1;
        user_air = -1;

        user_indoor_or_outdoor = -1;
        user_day_or_night = -1;
        act = -1;
        confidence_act = -1;

        location = null;

        algorithm_rain_level = -1;
        algorithm_rain_volume = -1;
        algorithm_indoor_or_outdoor = -1;
    }

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public int getUser_day_or_night() {
        return user_day_or_night;
    }

    public void setUser_day_or_night(int user_day_or_night) {
        this.user_day_or_night = user_day_or_night;
    }

    public int getUser_humidity_level() {
        return user_humidity_level;
    }

    public void setUser_humidity_level(int user_humidity_level) {
        this.user_humidity_level = user_humidity_level;
    }

    public int getUser_temp() {
        return user_temp;
    }

    public void setUser_temp(int user_temp) {
        this.user_temp = user_temp;
    }

    public int getUser_wind_level() {
        return user_wind_level;
    }

    public String getUser_wind_levelString() {
        return user_wind_level == -1 ? "?" : String.valueOf(user_wind_level);
    }


    public void setUser_wind_level(int user_wind_level) {
        this.user_wind_level = user_wind_level;
    }

    public int getUser_air() {
        return user_air;
    }

    public String getUser_airString() {
        return user_air == -1 ? "?" : String.valueOf(user_air);
    }

    public void setUser_air(int user_air) {
        this.user_air = user_air;
    }

    public int getAct() {
        return act;
    }

    public String getActString() {
        return act == -1 ? "?" : Constants.getActivityString(act);
    }

    public void setAct(int act) {
        this.act = act;
    }

    public int getConfidence_act() {
        return confidence_act;
    }

    public String getConfidenceActString() {
        return confidence_act == -1 ? "?" : String.valueOf(confidence_act);
    }


    public void setConfidence_act(int confidence_act) {
        this.confidence_act = confidence_act;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Loc getLocation() {
        return location;
    }

    public int getUser_rain_level() {
        return user_rain_level;
    }

    public void setUser_rain_level(int user_rain_level) {
        this.user_rain_level = user_rain_level;
    }

    public int getUser_indoor_or_outdoor() {
        return user_indoor_or_outdoor;
    }

    public void setUser_indoor_or_outdoor(int user_indoor_or_outdoor) {
        this.user_indoor_or_outdoor = user_indoor_or_outdoor;
    }

    public int getAlgorithm_rain_level() {
        return algorithm_rain_level;
    }

    public void setAlgorithm_rain_level(int algorithm_rain_level) {
        this.algorithm_rain_level = algorithm_rain_level;
    }

    public int getAlgorithm_indoor_or_outdoor() {
        return algorithm_indoor_or_outdoor;
    }

    public void setAlgorithm_indoor_or_outdoor(int algorithm_indoor_or_outdoor) {
        this.algorithm_indoor_or_outdoor = algorithm_indoor_or_outdoor;
    }

    public double getAlgorithm_rain_volume() {
        return algorithm_rain_volume;
    }

    public void setAlgorithm_rain_volume(double algorithm_rain_volume) {
        this.algorithm_rain_volume = algorithm_rain_volume;
    }

    public void setLocation(Loc loc) {
        this.location = loc;
    }
//    public void setLocation(double lat, double lng, double acc)
//    {
//        if(location == null)
//            location = new Loc(lat,lng,acc);
//        else
//            this.location.set(lat, lng, acc);
//    }

    public boolean hasLocation()
    {
        return location != null;

    }

    public LatLng getLatLng()
    {
        if(hasLocation())
            return new LatLng(location.getLatitude(), location.getLongitude());

        return null;
    }

    public boolean isHideOnMap()
    {
        return false;
    }

    @Override
    public String toString() {
        return id +
                ',' + user_rain_level +
                ',' + user_temp +
                ',' + user_wind_level +
                ',' + user_humidity_level +
                ',' + user_indoor_or_outdoor +
                ',' + user_day_or_night +
                ',' + user_air +
                ',' + act +
                ',' + confidence_act;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return uuid.equals(user.uuid);

    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}
