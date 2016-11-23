package com.example.karan.simpleweather;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class WeatherHelper {

    private static final String OPEN_WEATHER_MAP = "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";
    private static final String API_KEY = "cf5a979c450b3fab65c51d0349479889";

    //TODO: Set Weather Icon

    public interface AsyncResponse {

        void processFinish(String out1, String out2, String out3);//, String out4);
    }

    public static class task extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;

        public task(AsyncResponse asyncResponse) {
            delegate = asyncResponse;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Unable to process JSON");
            }

            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");

                    String city = json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.0f", main.getDouble("temp")) + "°C";
                    //TODO: setWeatherIcon()

                    delegate.processFinish(city, description, temperature);
                }
            } catch (JSONException e) {
                Log.e("Error", "Cannot process JSON", e);
            }
        }
    }


    public static JSONObject getWeatherJSON(String latitude, String longitude) {
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP, latitude, longitude));
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.addRequestProperty("x-api-key", API_KEY);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);

            String temp;
            while((temp=reader.readLine()) != null) {
                json.append(temp).append("\n");
            }
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            if(data.getInt("cod") != 200) {
                return null;
            }

            return data;

        } catch(Exception e) {
            Log.e("Error", "Unable to get URL");
            return null;
        }
    }

}
