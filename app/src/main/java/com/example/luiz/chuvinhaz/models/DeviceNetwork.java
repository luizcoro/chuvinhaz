package com.example.luiz.chuvinhaz.models;

import com.example.luiz.chuvinhaz.utils.Constants;
import com.example.luiz.chuvinhaz.utils.Utils;

/**
 * Created by luiz on 8/16/15.
 */
public class DeviceNetwork {
    private int network_type;
    private int lac;
    private int cid;
    private int psc;
    private int mcc;
    private int mnc;

    private int gsm_str;

    public DeviceNetwork() {
        network_type = -1;
        gsm_str = -9999;
        cid = -1;
        lac = -1;
        psc = -1;
        mcc = -1;
        mnc = -1;
    }

    public int getNetwork_type() {
        return network_type;
    }

    public void setNetwork_type(int network_type) {
        this.network_type = network_type;
    }

    public int getLac() {
        return lac;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getMcc() {
        return mcc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public int getPsc() {
        return psc;
    }

    public void setPsc(int psc) {
        this.psc = psc;
    }

    public int getMnc() {
        return mnc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public int getGsm_str() {
        return gsm_str;
    }

    public void setGsm_str(int gsm_str) {
        this.gsm_str = gsm_str;
    }

    public String getNetworkTypeString()
    {
        return (network_type == -1? "?" : Constants.getNetworkTypeString(network_type));
    }

    public String getLacString()
    {
        return Utils.attributeToString(lac, -1);
    }

    public String getCidString()
    {
        return Utils.attributeToString(cid, -1);
    }

    public String getPscString()
    {
        return Utils.attributeToString(psc, -1);
    }

    public String getMccString() {
        return Utils.attributeToString(mcc, -1);
    }

    public String getMncString() {
        return Utils.attributeToString(mnc, -1);
    }

    public String getGsm_strString()
    {
        return Utils.attributeToString(gsm_str, -9999);
    }


}
