package com.uoit.calvin.mytodo;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
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

    Date convertDate(String dateStr) {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.CANADA);
        Date date = null;
        if (dateStr.length() > 0) {
            try {
                date = df.parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        return date;

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
        String address1 = "";
        String address2 = "";
        if (address != null) {
            if (address.get(0).getAddressLine(0) != null) {
                address1 = address.get(0).getAddressLine(0);
            }
            if (address.get(0).getAddressLine(1) != null) {
                address2 = address.get(0).getAddressLine(1);
            }
        }

        return (address1 + " " + address2);
    }

    List<Task> getTask(String mode, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        List<Task> task;
        switch (mode){
            case "ALL":
                task = dbHelper.getAllShowData();
                break;
            case "COMPLETED":
                task = dbHelper.getAllCompletedData();
                break;
            case "INCOMPLETE":
                task = dbHelper.getAllIncompleteData();
                break;
            case "SHOW HIDDEN":
                task = dbHelper.getAllHiddenData();
                break;
            default:
                task = dbHelper.getAllShowData();
        }
        return task;
    }



}
