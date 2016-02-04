package com.example.luiz.chuvinhaz.models;

import com.example.luiz.chuvinhaz.utils.Utils;

/**
 * Created by luiz on 8/16/15.
 */
public class Sensors {
    private double ambient_temp;
    private double light;
    private double pressure;
    private double relative_humidity;
    private double proximity;
    private double[] accelerometer;

    private int ac_ambient_temp;
    private int ac_light;
    private int ac_pressure;
    private int ac_relative_humidity;
    private int ac_proximity;
    private int ac_Accelerometer;

    public Sensors() {
        ambient_temp = -1d;
        light = -1d;
        pressure = -1d;
        relative_humidity = -1d;
        proximity = -1d;
        accelerometer = new double[]{-1d,-1d,-1d};

        ac_ambient_temp = -1;
        ac_light = -1;
        ac_pressure = -1;
        ac_relative_humidity = -1;
        ac_proximity = -1;
        ac_Accelerometer = -1;
    }

    public double getAmbient_temp() {
        return ambient_temp;
    }

    public void setAmbient_temp(double ambient_temp) {
        this.ambient_temp = ambient_temp;
    }

    public double getLight() {
        return light;
    }

    public void setLight(double light) {
        this.light = light;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getRelative_humidity() {
        return relative_humidity;
    }

    public void setRelative_humidity(double relative_humidity) {
        this.relative_humidity = relative_humidity;
    }

    public double getProximity() {
        return proximity;
    }

    public void setProximity(double proximity) {
        this.proximity = proximity;
    }

    public double[] getAccelerometer() {
        return accelerometer;
    }

    public void setAccelerometer(double[] accelerometer) {
        this.accelerometer = accelerometer;
    }

    public int getAc_ambient_temp() {
        return ac_ambient_temp;
    }

    public void setAc_ambient_temp(int ac_ambient_temp) {
        this.ac_ambient_temp = ac_ambient_temp;
    }

    public int getAc_light() {
        return ac_light;
    }

    public void setAc_light(int ac_light) {
        this.ac_light = ac_light;
    }

    public int getAc_pressure() {
        return ac_pressure;
    }

    public void setAc_pressure(int ac_pressure) {
        this.ac_pressure = ac_pressure;
    }

    public int getAc_relative_humidity() {
        return ac_relative_humidity;
    }

    public void setAc_relative_humidity(int ac_relative_humidity) {
        this.ac_relative_humidity = ac_relative_humidity;
    }

    public int getAc_proximity() {
        return ac_proximity;
    }

    public void setAc_proximity(int ac_proximity) {
        this.ac_proximity = ac_proximity;
    }

    public int getAc_Accelerometer() {
        return ac_Accelerometer;
    }

    public void setAc_Accelerometer(int ac_Accelerometer) {
        this.ac_Accelerometer = ac_Accelerometer;
    }

    public double getAccelerometerX()
    {
        return accelerometer[0];
    }
    public double getAccelerometerY()
    {
        return accelerometer[1];
    }
    public double getAccelerometerZ()
    {
        return accelerometer[2];
    }
    public void setAccelerometer(double x, double y, double z)
    {
        this.accelerometer[0] = x;
        this.accelerometer[1] = y;
        this.accelerometer[2] = z;
    }

    public String getAmbient_tempString()
    {
        return Utils.attributeToString(ambient_temp, -1d);
    }

    public String getLightString()
    {
        return Utils.attributeToString(light, -1d);
    }

    public String getPressureString()
    {
        return Utils.attributeToString(pressure, -1d);
    }

    public String getRelative_humidityString()
    {
        return Utils.attributeToString(relative_humidity, -1d);
    }

    public String getProximityString()
    {
        return Utils.attributeToString(proximity, -1d);
    }

    public String getAccelerometerXString()
    {
        return Utils.attributeToString(accelerometer[0], -1d);
    }

    public String getAccelerometerYString()
    {
        return Utils.attributeToString(accelerometer[1], -1d);
    }

    public String getAccelerometerZString()
    {
        return Utils.attributeToString(accelerometer[2], -1d);
    }

    public String getAc_ambient_tempString()
    {
        return Utils.attributeToString(ac_ambient_temp, -1);
    }

    public String getAc_lightString()
    {
        return Utils.attributeToString(ac_light, -1);
    }

    public String getAc_pressureString()
    {
        return Utils.attributeToString(ac_pressure, -1);
    }

    public String getAc_relative_humidityString()
    {
        return Utils.attributeToString(ac_relative_humidity, -1);
    }

    public String getAc_proximityString()
    {
        return Utils.attributeToString(ac_proximity, -1);
    }

    public String getAc_accelerometerString()
    {
        return Utils.attributeToString(ac_Accelerometer, -1);
    }

}
