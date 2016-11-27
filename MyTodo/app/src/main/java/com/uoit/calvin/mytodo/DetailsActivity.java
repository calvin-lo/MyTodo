package com.uoit.calvin.mytodo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    long ID;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ID =  Long.parseLong(getIntent().getStringExtra("ID"));
        dbHelper = new DBHelper(this);
        Task task = dbHelper.getSingleData(ID);

        TextView titleView = (TextView) findViewById(R.id.textViewTitle);
        TextView detailsView = (TextView) findViewById(R.id.textViewDetails);
        TextView addressView = (TextView) findViewById(R.id.textViewAddress);
        TextView weatherCity = (TextView) findViewById(R.id.txtCity);
        TextView weatherDetails = (TextView) findViewById(R.id.txtDetails);
        TextView weatherTemperature = (TextView) findViewById(R.id.txtTemperature);
        TextView timeView = (TextView) findViewById(R.id.textViewTime);
        TextView selectedView = (TextView) findViewById(R.id.textViewSelected);
        TextView dueView = (TextView) findViewById(R.id.textViewDueTimestamp);

        titleView.setText(task.getTitle());
        detailsView.setText(task.getDetails());


        if (task.getLatitude() != 0.0 && task.getLongitude() != 0.0) {
            addressView.setText(new Helper().getAddress(this, task.getLatitude(), task.getLongitude()));
        }

        weatherCity.setText(task.getWeather().getCity());
        weatherDetails.setText(task.getWeather().getDetails());
        weatherTemperature.setText(task.getWeather().getTemperature());

        timeView.setText(task.getTimestamp());


        selectedView.setText(task.getSelected());

        if (task.getDueTimestamp().length() == 0) {
            String text = "No End Time";
            dueView.setText(text);
        } else {
            dueView.setText(task.getDueTimestamp());
        }
    }


}
