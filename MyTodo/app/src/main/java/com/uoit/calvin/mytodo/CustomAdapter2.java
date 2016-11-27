package com.uoit.calvin.mytodo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by calvin on 27/11/16.
 */

public class CustomAdapter2 extends BaseAdapter{

    private Context mContext;
    private String[]  title;
    private int[] icon;

    CustomAdapter2(Context context, String[] text1, int[] imageIds) {
        mContext = context;
        title = text1;
        icon = imageIds;

    }

    public int getCount() {
        return title.length;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            View row;
            row = inflater.inflate(R.layout.row, parent, false);
            TextView tv;
            ImageView iv;
            iv = (ImageView) row.findViewById(R.id.imgIcon);
            tv = (TextView) row.findViewById(R.id.txtTitle);
            tv.setText(title[position]);
            iv.setImageResource(icon[position]);
            return row;
        } else {
            return convertView;
        }
    }
}
