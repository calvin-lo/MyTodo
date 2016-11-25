package com.uoit.calvin.finalproject;

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
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity  implements LocationListener {
    DBHelper dbHelper;
    DrawerLayout mDrawerLayout;
    CustomAdapter dataAdapter = null;

    List<String> taskList;
    List<Long> taskIds;
    ListView tasksListView;
    ListView idListView;

    protected LocationManager locationManager;
    Location location;
    private static final int SAVING_DATA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        deleteDatabase("tasksDB");

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        FloatingActionButton btnFab = (FloatingActionButton) findViewById(R.id.fab1);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddActivity.class);
                startActivityForResult(intent,SAVING_DATA);
            }
        });



        //setupTabLayout();
        updateDrawer();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Task task = new Task();
        dbHelper = new DBHelper(this);
        if(requestCode == SAVING_DATA && resultCode == RESULT_OK) {
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
        dbHelper = new DBHelper(this);

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
        dataAdapter = new CustomAdapter(this, R.layout.activity_listview, task);
        tasksListView = (ListView) findViewById(R.id.taskList);
        tasksListView.setAdapter(dataAdapter);
        registerForContextMenu(tasksListView);

        // Set the ID
        ArrayAdapter arrayAdapterID = new ArrayAdapter<>(this, R.layout.activity_listview, taskIds);
        idListView= (ListView) findViewById(R.id.taskListID);
        idListView.setAdapter(arrayAdapterID);
        registerForContextMenu(idListView);

        dbHelper.close();
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
        if (menuItemName.equals("Delete")) {
            ListView l = (ListView)findViewById(R.id.taskListID);
            String ID = l.getItemAtPosition(info.position).toString();
            dbHelper = new DBHelper(this);
            dbHelper.deleteTransactions((Long.parseLong(ID)));
            displayTaskList();
        } else if (menuItemName.equals("Edit")) {
            Intent intent = new Intent(this, DetailsActivity.class);
            startActivity(intent);
        }

        return true;
    }


    /*
        Drawer
     */

    public void updateDrawer() {
        String[] mPlanetTitles;
        ListView mDrawerList;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mPlanetTitles = new String[1];
        mPlanetTitles[0] = "Test";

        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        if (mDrawerList != null) {
            mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer, mPlanetTitles));
        }


        //  Setup Drawer Toggle of the Toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    /*
        Location
     */

    public void updateLocation() {
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (checkLocationPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }
    }

    public String getAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> address = new ArrayList<>();

        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            address = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String address1 = address.get(0).getAddressLine(0);
        String address2 = address.get(0).getAddressLine(1);

        return (address1 + " " + address2);
    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        return (this.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onLocationChanged(Location location) {

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
            TextView time;
            TextView location;
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
                holder.time = (TextView) convertView.findViewById(R.id.time);
                holder.location = (TextView) convertView.findViewById(R.id.location);
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
            holder.time.setText("\t(" + task.getTimestamp() + " )");
            holder.location.setText("\t" + getAddress(task.getLatitude(),task.getLongitude()) + ")");
            holder.name.setText(task.getTitle());
            holder.name.setChecked(task.isSelected());
            holder.name.setTag(task);

            return convertView;

        }
    }


}

