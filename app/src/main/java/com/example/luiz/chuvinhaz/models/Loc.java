package com.example.luiz.chuvinhaz.models;

/**
 * Created by luiz on 01/05/15.
 */
public class Loc {
    private double latitude;
    private double longitude;
    private float accuracy;
    private double altitude;
    private float speed;

    public Loc(float accuracy, double latitude, double longitude, double altitude, float speed) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.accuracy = accuracy;
        this.altitude = altitude;
        this.speed = speed;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public float getAccuracy() {
        return accuracy;
    }

    public double getAltitude() {
        return altitude;
    }

    public float getSpeed() {
        return speed;
    }


    public void set(float acc, double lat, double lng, double alt, float speed) {
        this.accuracy = acc;
        this.latitude = lat;
        this.longitude = lng;
        this.altitude = alt;
        this.speed = speed;
    }

    public void setLat(double lat) {
        this.latitude = lat;
    }

    public void setLng(double lng) {
        this.longitude = lng;
    }

    public void setAcc(float acc) {
        this.accuracy = acc;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
