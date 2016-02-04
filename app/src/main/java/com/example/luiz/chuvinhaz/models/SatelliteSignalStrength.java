package com.example.luiz.chuvinhaz.models;

/**
 * Created by luiz on 5/27/15.
 */
public class SatelliteSignalStrength {
    private int pnr;
    private float signal_strength;

    public SatelliteSignalStrength(int pnr, float signal_strength) {
        this.pnr = pnr;
        this.signal_strength = signal_strength;
    }

    public int getPnr() {
        return pnr;
    }

    public void setPnr(int pnr) {
        this.pnr = pnr;
    }

    public float getSignal_strength() {
        return signal_strength;
    }

    public void setSignal_strength(float signal_strength) {
        this.signal_strength = signal_strength;
    }
}
