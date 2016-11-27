package com.uoit.calvin.mytodo;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class SettingActivity extends AppCompatActivity {

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

        String[] settingList = getResources().getStringArray(R.array.setting_list);
        int[] drawableIds = {R.drawable.ic_delete_forever_black_24dp};


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
                            break;
                    }

                }
            });
        }

    }
}
