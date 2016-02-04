package com.example.luiz.chuvinhaz.models;

/**
 * Created by luiz on 5/27/15.
 */
public class OperatorSignalStrength {

    private int type;
    private int cid;
    private int lac;
    private int psc;
    private int signal_strength;

    public OperatorSignalStrength(int type, int cid, int lac, int psc, int signal_strength) {
        this.type = type;
        this.cid = cid;
        this.lac = lac;
        this.psc = psc;
        this.signal_strength = signal_strength;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getPsc() {
        return psc;
    }

    public void setPsc(int psc) {
        this.psc = psc;
    }

    public int getSignal_strength() {
        return signal_strength;
    }

    public void setSignal_strength(int signal_strength) {
        this.signal_strength = signal_strength;
    }
}
