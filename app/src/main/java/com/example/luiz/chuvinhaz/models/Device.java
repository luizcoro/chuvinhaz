package com.example.luiz.chuvinhaz.models;

import com.example.luiz.chuvinhaz.utils.Constants;
import com.example.luiz.chuvinhaz.utils.Utils;

/**
 * Created by luiz on 8/16/15.
 */
public class Device {

    private String name;
    private int type;
    private double screen_size;

    public Device() {
        name = "?";
        type = -1;
        screen_size = -1d;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double getScreen_size() {
        return screen_size;
    }

    public void setScreen_size(double screen_size) {
        this.screen_size = screen_size;
    }

    public String getTypeString()
    {
        return (type == -1 ? "?" : Constants.getPhoneTypeString(type));
    }

    public String getScreen_sizeString()
    {
        return Utils.attributeToString(Utils.round(screen_size, 2), -1d);
    }

}
