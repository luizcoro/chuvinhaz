package com.example.luiz.chuvinhaz.models;

/**
 * Created by luiz on 5/27/15.
 */
public class WifiSignalStrength {

    private int frequency;
    private int rssi;

    public WifiSignalStrength(int frequency, int rssi) {
        this.frequency = frequency;
        this.rssi = rssi;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
