package com.uoit.calvin.mytodo;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    SharedPreferences mPrefs;
    final String welcomeScreenShownPref = "welcomeScreenShown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String[] settingList = getResources().getStringArray(R.array.setting_list);
        int[] drawableIds = {R.drawable.ic_clear_all_white_18dp, R.drawable.ic_star_white_18dp};

        final Toast t1= Toast.makeText(this, "Deleted Database", Toast.LENGTH_SHORT);
        final Toast t2= Toast.makeText(this, "Hint will show next time you open the app", Toast.LENGTH_SHORT);
        CustomAdapter2 adapter = new CustomAdapter2(this,  settingList, drawableIds);
        ListView settingListView = (ListView) findViewById(R.id.settingList);
        if (settingListView != null) {
            settingListView.setAdapter(adapter);
            registerForContextMenu(settingListView);

            settingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id){
                    switch (position) {
                        // Reset on Database
                        case 0:
                            deleteDatabase("tasksDB");
                            t1.show();
                            break;
                        case 1:
                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.putBoolean(welcomeScreenShownPref, false);
                            editor.apply();
                            t2.show();

                            break;
                    }

                }
            });
        }

    }
}
