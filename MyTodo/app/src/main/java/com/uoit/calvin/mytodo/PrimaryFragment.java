package com.uoit.calvin.mytodo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;


public class PrimaryFragment extends Fragment implements LocationListener {


    View v;
    DBHelper dbHelper;

    CustomAdapter dataAdapter = null;


    List<String> taskList;
    List<Long> taskIds;
    ListView tasksListView;
    ListView idListView;

    protected LocationManager locationManager;
    Location location;
    private static final int SAVING_DATA = 1;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.primary_layout,null);

        FloatingActionButton btnFab = (FloatingActionButton) v.findViewById(R.id.fab1);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivityForResult(intent,SAVING_DATA);
            }
        });


        displayTaskList();
        updateLocation();

        return v;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId() == R.id.taskList) {
            String[] menuItems;
            if (((MainActivity)getActivity()).getMode().equals("SHOW HIDDEN")) {
                 menuItems = getResources().getStringArray(R.array.update_hidden_menu);
            } else {
                menuItems = getResources().getStringArray(R.array.update_menu);
            }
            for (int i = 0; i< menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems;
        if (((MainActivity)getActivity()).getMode().equals("SHOW HIDDEN")) {
            menuItems = getResources().getStringArray(R.array.update_hidden_menu);
        } else {
            menuItems = getResources().getStringArray(R.array.update_menu);
        }
        String menuItemName = menuItems[menuItemIndex];
        ListView l = (ListView)v.findViewById(R.id.taskListID);
        String ID = l.getItemAtPosition(info.position).toString();
        if (menuItemName.equals("Delete")) {
            dbHelper = new DBHelper(getContext());
            dbHelper.deleteTransactions((Long.parseLong(ID)));
            dbHelper.close();
        } else if (menuItemName.equals("Details")) {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("ID", ID);
            startActivity(intent);
        } else if (menuItemName.equals("Hide")) {
            dbHelper = new DBHelper(getContext());
            dbHelper.updateShow(Long.parseLong(ID), true);
            dbHelper.close();
        } else if (menuItemName.equals("Unhide")) {
            dbHelper = new DBHelper(getContext());
            dbHelper.updateShow(Long.parseLong(ID), false);
            dbHelper.close();
            Log.i("HIIIIIII", "HERE");
        }
        displayTaskList();


        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Task task = new Task();
        dbHelper = new DBHelper(getContext());
        if(requestCode == SAVING_DATA && resultCode == getActivity().RESULT_OK) {
            updateLocation();
            task.setTitle(data.getExtras().getString("result"));
            task.setTimestamp(new Helper().getCurrentTime());
            task.setHidden(false);
            task.setCompleted(false);
            task.setSelected(false);
            task.setDueTimestamp(data.getExtras().getString("dueTimestamp"));
            if (location != null) {
                task.setLatitude(location.getLatitude());
                task.setLongitude(location.getLongitude());
                task.setWeather(getWeather(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())));
            } else {
                task.setLatitude(0.0);
                task.setLongitude(0.0);
                task.setWeather(new Weather());
            }
            dbHelper.addTransactions(task);
        }
        dbHelper.close();

        displayTaskList();
    }

    public void displayTaskList() {
        dbHelper = new DBHelper(getContext());

        List<Task> task = new Helper().getTask(((MainActivity)getActivity()).getMode(), getContext());
        taskList = new ArrayList<>();
        taskIds = new ArrayList<>();
        for (Task t : task) {
            taskList.add(t.getTitle());
        }
        for (Task t : task) {
            taskIds.add(t.getId());
        }

        // Set the task
        Collections.reverse(task);
        dataAdapter = new CustomAdapter(getContext(), R.layout.activity_listview, task);
        tasksListView = (ListView) v.findViewById(R.id.taskList);
        tasksListView.setAdapter(dataAdapter);
        registerForContextMenu(tasksListView);

        // Set the ID
        Collections.reverse(taskIds);
        ArrayAdapter arrayAdapterID = new ArrayAdapter<>(getContext(), R.layout.activity_listview, taskIds);
        idListView= (ListView) v.findViewById(R.id.taskListID);
        idListView.setAdapter(arrayAdapterID);
        registerForContextMenu(idListView);

        dbHelper.close();
    }


    /*
        Weather
     */

    public Weather getWeather(String latitude, String longitude) {


        WeatherHelper.task asyncTask = new WeatherHelper.task(new WeatherHelper.AsyncResponse() {
            public void processFinish(String city_weather, String description_weather, String temperature_weather) {
            }
        });

        Weather weather = new Weather();

        try {

            JSONObject json = asyncTask.execute(latitude, longitude).get();
            if(json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");

                    String city = json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                    String description = details.getString("description").toUpperCase(Locale.US);
                    String temperature = String.format("%.0f", main.getDouble("temp")) + "°C";


                    weather.setCity(city);
                    weather.setDetails(description);
                    weather.setTemperature(temperature);

                }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weather;
    }
     /*
        Location
     */
    public void updateLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
            //location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        return (getActivity().checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        TextView textView = (TextView) v.findViewById(R.id.textViewAddressLabel);
/*        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            String label = "Current Address:" + new Helper().getAddress(getContext(), location.getLatitude(), location.getLongitude());
            textView.setText(label);
        }*/
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }



}
