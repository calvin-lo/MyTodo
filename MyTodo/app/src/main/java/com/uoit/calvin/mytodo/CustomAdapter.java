package com.uoit.calvin.mytodo;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;


public class CustomAdapter extends ArrayAdapter<Task> {


    private ArrayList<Task> taskList;
    private DBHelper dbHelper;

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
    public View getView(final int position, View convertView, ViewGroup parent) {

        CustomAdapter.ViewHolder holder;


        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.task_info, null);

            holder = new CustomAdapter.ViewHolder();
            holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
            convertView.setTag(holder);
            dbHelper = new DBHelper(getContext());
            holder.name.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    Task task = (Task) cb.getTag();
                    task.setSelected(cb.isChecked());
                    if (cb.isChecked()) {
                        dbHelper.updateSelected(task.getId(), true);
                        dbHelper.updateCompleted(task.getId(), true);
                    } else {
                        dbHelper.updateSelected(task.getId(), false);
                        dbHelper.updateCompleted(task.getId(), false);
                    }
                }
            });
        }
        else {
            holder = (CustomAdapter.ViewHolder) convertView.getTag();
        }

        Task task = taskList.get(position);
        holder.name.setText(task.getTitle());
        holder.name.setChecked(task.isSelected());
        holder.name.setTag(task);

        dbHelper.close();

        return convertView;

    }
}
