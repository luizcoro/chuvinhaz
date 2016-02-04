package com.example.luiz.chuvinhaz.managers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.example.luiz.chuvinhaz.models.Loc;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by luiz on 02/05/15.
 */
public class MyLocationManager {

    public static Loc getBestLocation(Loc location1, Loc location2) {
        if (location1 == null && location2 == null)
            return null;
        else if (location1 == null)
            return location2;
        else if (location2 == null)
            return location1;
        else {
            if (location1.getAccuracy() > location2.getAccuracy())
                return location1;
            else
                return location2;
        }
    }

    public static LatLng getLocationFromAddress(Context context,String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p1;
    }
}
