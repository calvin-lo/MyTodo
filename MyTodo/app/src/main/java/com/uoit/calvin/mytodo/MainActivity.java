package com.uoit.calvin.mytodo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;

    ViewPagerAdapter adapter;

    String mode;

    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mode = "ALL";

        // Set up the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String permission = "android.permission.ACCESS_FINE_LOCATION";
        String permission2 = "android.permission.ACCESS_COARSE_LOCATION";
        if (this.checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (this.checkCallingOrSelfPermission(permission2) == PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        final ListView mDrawerList;
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        if (mDrawerList != null) {
            mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case 0:
                            mode = "ALL";
                            break;
                        case 1:
                            mode = "COMPLETED";
                            break;
                        case 2:
                            mode = "INCOMPLETE";
                            break;
                        case 3:
                            mode = "SHOW HIDDEN";
                            break;
                        default:
                            mode = "ALL";
                    }
                    mDrawerLayout.closeDrawer(mDrawerList);
                    adapter.notifyDataSetChanged();
                }
            });
        }



        setupTabLayout();
        updateDrawer();

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);
        if (!welcomeScreenShown) {
            String welcome = getResources().getString(R.string.welcomeTitle);
            String description = getResources().getString(R.string.description);
            new AlertDialog.Builder(this)
                    .setTitle(welcome)
                    .setMessage(description)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Don't show this again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putBoolean(welcomeScreenShownPref, true);
                            editor.apply();
                            dialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateDrawer();
        setupTabLayout();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        updateDrawer();
        setupTabLayout();
        adapter.notifyDataSetChanged();
    }


    public String getMode() {
        return this.mode;
    }



    /*
    Tab Layout
    */
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new PrimaryFragment(), getResources().getString(R.string.fragOne));
        adapter.addFragment(new SecondFragment(), getResources().getString((R.string.fragTwo)));
        viewPager.setAdapter(adapter);
    }

    public void setupTabLayout() {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        if (tabLayout != null) {
            tabLayout.setupWithViewPager(viewPager);
            //tabLayout.getTabAt(0).setIcon(R.drawable.ic_dns_white_24dp);
            //tabLayout.getTabAt(1).setIcon(R.drawable.ic_donut_small_black_24dp);

            tabLayout.setOnTabSelectedListener(
                    new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            super.onTabSelected(tab);
                            switch (tab.getPosition()) {
                                case 0:
                                    //tab.setIcon(R.drawable.ic_dns_white_24dp);
                                    break;
                                case 1:
                                    //tab.setIcon(R.drawable.ic_donut_small_white_24dp);
                                    break;
                            }
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {
                            super.onTabUnselected(tab);
                            switch (tab.getPosition()) {
                                case 0:
                                    //tab.setIcon(R.drawable.ic_dns_black_24dp);
                                    break;
                                case 1:
                                    //tab.setIcon(R.drawable.ic_donut_small_black_24dp);
                                    break;
                            }
                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {
                            super.onTabReselected(tab);
                        }
                    }
            );
        }

    }


    /*
        Drawer
     */

    public void updateDrawer() {
        String[] mPlanetTitles;
        ListView mDrawerList;
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mPlanetTitles = getResources().getStringArray(R.array.drawer_menu);
        int[] drawableIds = {R.drawable.ic_grade_white_18dp,
                                R.drawable.ic_check_white_12dp,
                                R.drawable.ic_remove_circle_outline_white_18dp,
                                R.drawable.ic_visibility_off_white_18dp};


        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        if (mDrawerList != null) {
            mDrawerList.setAdapter(new CustomAdapter2(this, mPlanetTitles, drawableIds));
            //mDrawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer, mPlanetTitles));
        }


        //  Setup Drawer Toggle of the Toolbar
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


        /*
        Setting Menu
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




}

