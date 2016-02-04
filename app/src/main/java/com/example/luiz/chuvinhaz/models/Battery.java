package com.example.luiz.chuvinhaz.models;

import com.example.luiz.chuvinhaz.utils.Constants;
import com.example.luiz.chuvinhaz.utils.Utils;

/**
 * Created by luiz on 8/16/15.
 */
public class Battery {

    private int health;
    private int plugged;
    private int status;
    private double percentage;
    private double capacity;
    private double temp;
    private int voltage;


    public Battery() {
        health = -1;
        plugged = -1;
        status = -1;
        percentage = -1d;
        capacity = -1d;
        temp = -1d;
        voltage = -1;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getPlugged() {
        return plugged;
    }

    public void setPlugged(int plugged) {
        this.plugged = plugged;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public double getCapacity() {
        return capacity;
    }

    public void setCapacity(double capacity) {
        this.capacity = capacity;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getVoltage() {
        return voltage;
    }

    public void setVoltage(int voltage) {
        this.voltage = voltage;
    }

    public String getHealthString()
    {
        return (health == -1 ? "?" : Constants.getBatteryHealth(health));
    }

    public String getPluggedString()
    {
        return (plugged == -1 ? "?" : Constants.getBatteryPlugged(plugged));
    }

    public String getStatusString()
    {
        return (status == -1 ? "?" : Constants.getBatteryStatus(status));
    }

    public String getCapacityString()
    {
        return (capacity == -1d ? "?" : String.valueOf(capacity));
    }

    public String getVoltageString()
    {
        return (voltage == -1 ? "?" : String.valueOf(voltage));
    }

    public String getPercentageString()
    {
        return Utils.attributeToString(Utils.round(percentage, 2), -1d);
    }


    public String getTempString()
    {
        return Utils.attributeToString(temp, -1d);
    }


}
