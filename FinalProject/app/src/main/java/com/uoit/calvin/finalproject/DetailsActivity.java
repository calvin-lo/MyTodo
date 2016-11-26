package com.uoit.calvin.finalproject;

import android.location.Address;
import android.location.Geocoder;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailsActivity extends AppCompatActivity {

    long ID;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ID =  Long.parseLong(getIntent().getStringExtra("ID"));
        dbHelper = new DBHelper(this);
        Task task = dbHelper.getSingelData(ID);

        TextView nameView = (TextView) findViewById(R.id.textViewName);
        TextView addressView = (TextView) findViewById(R.id.textViewAddress);

        nameView.setText(task.getTitle());

       // addressView.setText(task.getLatitude() + " ; " + task.getLongitude());

        if (task.getLatitude() != 0.0 && task.getLongitude() != 0.0) {

            addressView.setText(new Helper().getAddress(this, task.getLatitude(), task.getLongitude()));
        }

    }


}
