package com.uoit.calvin.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        setupTabLayout();
        updateDrawer();
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


}

