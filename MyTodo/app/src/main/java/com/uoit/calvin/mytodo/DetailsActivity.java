package com.uoit.calvin.mytodo;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    long ID;
    DBHelper dbHelper;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ID =  Long.parseLong(getIntent().getStringExtra("ID"));
        dbHelper = new DBHelper(this);
        task = dbHelper.getSingleData(ID);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            toolBar.setTitle(task.getTitle());
        }
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        setDetails();
        setAddress();
        setTime();
        setDue();
        setStatus();
        setWeather();
    }

    public void setDetails() {
        TextView detailsView = (TextView) findViewById(R.id.textViewDetails);
        detailsView.setText(task.getDetails());
        detailsView.setMovementMethod(new ScrollingMovementMethod());

    }

    public void setAddress() {
        TextView addressView = (TextView) findViewById(R.id.textViewAddress);

        String address;
        if (task.getLatitude() != 0.0 && task.getLongitude() != 0.0) {
            address =  new Helper().getAddress(this, task.getLatitude(), task.getLongitude());
        } else {
            address = "No Location";
        }
        addressView.setText(address);
        addressView.setMovementMethod(new ScrollingMovementMethod());


    }

    public void setTime() {
        TextView timeView = (TextView) findViewById(R.id.textViewTime);

        timeView.setText(task.getTimestamp());

    }

    public void setDue() {
        TextView dueView = (TextView) findViewById(R.id.textViewDueTimestamp);

        if (task.getDueTimestamp().length() == 0) {
            String text = "No End Time";
            dueView.setText(text);
        } else {
            dueView.setText(task.getDueTimestamp());
        }


    }

    public void setStatus() {
        TextView selectedView = (TextView) findViewById(R.id.textViewSelected);

        String text;
        if (task.isCompleted()) {
            text = "COMPLETE";
        } else {
            text = "INCOMPLETE";
        }

        selectedView.setText(text);

    }

    public void setWeather() {
        TextView weatherView = (TextView) findViewById(R.id.textViewWeather);
        String weatherText = task.getWeather().getTemperature() + " | " + task.getWeather().getDetails();
        weatherView.setText(weatherText);
    }

    public void clickDone(View v) {
        DBHelper dbHelper = new DBHelper(this.getApplicationContext());
        dbHelper.updateCompleted(ID, true);
        dbHelper.updateSelected(ID, true);
        TextView selectedView = (TextView) findViewById(R.id.textViewSelected);
        String text = "COMPLETE";
        selectedView.setText(text);
    }


}
