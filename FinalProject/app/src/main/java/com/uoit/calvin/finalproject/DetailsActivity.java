package com.uoit.calvin.finalproject;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailsActivity extends AppCompatActivity {

    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setupTabLayout();
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
}
