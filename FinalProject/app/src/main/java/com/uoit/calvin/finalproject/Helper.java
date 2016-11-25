package com.uoit.calvin.finalproject;

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


}
