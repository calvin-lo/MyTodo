package com.uoit.calvin.finalproject;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

class Helper {

    public String getCurrentTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.CANADA);
        Date date = new Date();
        return dateFormat.format(date);
    }

    public String getAddress(Context context, double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> address = new ArrayList<>();

        geocoder = new Geocoder(context, Locale.getDefault());
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address1 = address.get(0).getAddressLine(0);
        String address2 = address.get(0).getAddressLine(1);

        return (address1 + " " + address2);
    }



}
