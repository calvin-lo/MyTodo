package com.uoit.calvin.mytodo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity  implements AdapterView.OnItemSelectedListener {

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

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.button, R.layout.custom_spinner);
        adapter.setDropDownViewResource(R.layout.custom_dropdown);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        dbHelper = new DBHelper(this.getApplicationContext());
        TextView selectedView = (TextView) findViewById(R.id.textViewSelected);
        String text;
        switch(pos) {
            case 0:
                break;
            case 1:
                dbHelper.updateCompleted(ID, true);
                dbHelper.updateSelected(ID, true);;
                text = "COMPLETE";
                selectedView.setText(text);
                break;
            case 2:
                dbHelper.updateCompleted(ID, false);
                dbHelper.updateSelected(ID, false);
                text = "INCOMPLETE";
                selectedView.setText(text);
                break;
            case 3:
                dbHelper.updateShow(ID, true);
                break;
            case 4:
                dbHelper.updateShow(ID, false);
                break;
            case 5:
                new AlertDialog.Builder(this)
                        .setMessage("Do you really want to delete this item?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dbHelper.deleteTransactions(ID);
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                break;
        }
        dbHelper.close();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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



}
