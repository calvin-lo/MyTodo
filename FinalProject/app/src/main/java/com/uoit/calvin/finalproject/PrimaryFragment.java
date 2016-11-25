package com.uoit.calvin.finalproject;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PrimaryFragment extends Fragment {

    DBHelper mydb;
    ListView transList;
    ListView idList;
    View x;
    static final int SAVING_DATA = 1;
    List<String> tags;
    List<Long> tagIds;
    CustomAdapter dataAdapter = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        x = inflater.inflate(R.layout.primary_layout,null);
        FloatingActionButton btnFab = (FloatingActionButton) x.findViewById(R.id.fab1);
        btnFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddActivity.class);
                startActivityForResult(intent,SAVING_DATA);
            }
        });
        /**
         * Databases
         */
        displayTaskList();

        return x;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Task task = new Task();
        mydb = new DBHelper(getContext());
        if(requestCode == SAVING_DATA && resultCode == getActivity().RESULT_OK) {
            task.setTitle(data.getExtras().getString("result"));
            task.setTimestamp(new Helper().getCurrentTime());
            mydb.addTransactions(task);
        }
        mydb.close();

        displayTaskList();
    }

    public void displayTaskList() {
        mydb = new DBHelper(getContext());

        List<Task> task = mydb.getAllData();
        tags = new ArrayList<>();
        tagIds = new ArrayList<>();
        for (Task t : task) {
            tags.add(t.getTitle());
        }
        for (Task t : task) {
            tagIds.add(t.getId());
        }

        // Set the task
        dataAdapter = new CustomAdapter(getContext(), R.layout.activity_listview, task);
        transList = (ListView) x.findViewById(R.id.taskList);
        transList.setAdapter(dataAdapter);
        registerForContextMenu(transList);

        // Set the ID
        ArrayAdapter arrayAdapterID = new ArrayAdapter<>(getContext(), R.layout.activity_listview,tagIds);
        idList = (ListView) x.findViewById(R.id.taskListID);
        idList.setAdapter(arrayAdapterID);
        registerForContextMenu(idList);

        mydb.close();
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
            ListView l = (ListView) x.findViewById(R.id.taskListID);
            String ID = l.getItemAtPosition(info.position).toString();
            mydb = new DBHelper(getContext());
            mydb.deleteTransactions((Long.parseLong(ID)));
            displayTaskList();
        }

        return true;
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
                holder.time = (TextView) convertView.findViewById(R.id.code);
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
            holder.name.setText(task.getTitle());
            holder.name.setChecked(task.isSelected());
            holder.name.setTag(task);

            return convertView;

        }
    }



}
