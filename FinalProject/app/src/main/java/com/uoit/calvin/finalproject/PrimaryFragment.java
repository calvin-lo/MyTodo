package com.uoit.calvin.finalproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PrimaryFragment extends Fragment implements LocationListener{


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
            String[] menuItems = getResources().getStringArray(R.array.update_menu);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        String[] menuItems = getResources().getStringArray(R.array.update_menu);
        String menuItemName = menuItems[menuItemIndex];
        ListView l = (ListView)v.findViewById(R.id.taskListID);
        String ID = l.getItemAtPosition(info.position).toString();
        if (menuItemName.equals("Delete")) {
            dbHelper = new DBHelper(getContext());
            dbHelper.deleteTransactions((Long.parseLong(ID)));
            displayTaskList();
        } else if (menuItemName.equals("Edit")) {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra("ID", ID);
            startActivity(intent);
        }

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
            if (location != null) {
                task.setLatitude(location.getLatitude());
                task.setLongitude(location.getLongitude());
            }
            dbHelper.addTransactions(task);
        }
        dbHelper.close();

        displayTaskList();
    }

    public void displayTaskList() {
        dbHelper = new DBHelper(getContext());

        List<Task> task = dbHelper.getAllData();
        taskList = new ArrayList<>();
        taskIds = new ArrayList<>();
        for (Task t : task) {
            taskList.add(t.getTitle());
        }
        for (Task t : task) {
            taskIds.add(t.getId());
        }

        // Set the task
        dataAdapter = new CustomAdapter(getContext(), R.layout.activity_listview, task);
        tasksListView = (ListView) v.findViewById(R.id.taskList);
        tasksListView.setAdapter(dataAdapter);
        registerForContextMenu(tasksListView);

        // Set the ID
        ArrayAdapter arrayAdapterID = new ArrayAdapter<>(getContext(), R.layout.activity_listview, taskIds);
        idListView= (ListView) v.findViewById(R.id.taskListID);
        idListView.setAdapter(arrayAdapterID);
        registerForContextMenu(idListView);

        dbHelper.close();
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
        if (location.getLatitude() != 0 && location.getLongitude() != 0) {
            String label = "Current Address:" + new Helper().getAddress(getContext(), location.getLatitude(), location.getLongitude());
            textView.setText(label);
        }
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




    /*
        custom adapter
    */
    private class CustomAdapter extends ArrayAdapter<Task> {

        private ArrayList<Task> taskList;

        CustomAdapter(Context context, int textViewResourceId, List<Task> taskList) {
            super(context, textViewResourceId, taskList);
            this.taskList = new ArrayList<Task>();
            this.taskList.addAll(taskList);
        }

        private class ViewHolder {
            CheckBox name;

        }

        @Override
        @NonNull
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.task_info, null);

                holder = new ViewHolder();
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Task task = (Task) cb.getTag();
                        task.setSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            Task task = taskList.get(position);
            holder.name.setText(task.getTitle());
            holder.name.setChecked(task.isSelected());
            holder.name.setTag(task);

            return convertView;

        }
    }



}
