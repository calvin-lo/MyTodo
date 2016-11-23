package com.example.karan.simpleweather;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView city, details, temperature;
    String latitude;
    String longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = (TextView) findViewById(R.id.txtCity);
        details = (TextView) findViewById(R.id.txtDetails);
        temperature = (TextView) findViewById(R.id.txtTemperature);

        GetWeather();
    }

    public void GetWeather() {
        WeatherHelper.task asyncTask = new WeatherHelper.task(new WeatherHelper.AsyncResponse() {
            public void processFinish(String city_weather, String description_weather, String temperature_weather) {

                city.setText(city_weather);
                details.setText(description_weather);
                temperature.setText(temperature_weather);
            }
        });

        //Enter Coordinates (Adjust to change city):
        latitude = "51.509865";
        longitude = "-0.118092";

        asyncTask.execute(latitude, longitude);
    }
}
