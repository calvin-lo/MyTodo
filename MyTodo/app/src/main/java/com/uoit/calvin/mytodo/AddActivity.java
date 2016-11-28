package com.uoit.calvin.mytodo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener{

    private String time;
    private String date;
    private String dueTimestamp;
    MediaPlayer clickSoundMP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        time = "";
        date = "";
        dueTimestamp = "";

        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        clickSoundMP = MediaPlayer.create(this, R.raw.mouse_click);

    }

    public void clickSave(View v) {
        clickSoundMP.start();
        Intent returnIntent = new Intent();
        EditText titleInput = (EditText) findViewById(R.id.titleInput);
        EditText detailsInput = (EditText) findViewById(R.id.detailsInput);
        String title = titleInput.getText().toString();
        String details = detailsInput.getText().toString();
        returnIntent.putExtra("title", title);
        returnIntent.putExtra("details", details);
        returnIntent.putExtra("dueTimestamp", dueTimestamp);
        setResult(RESULT_OK,returnIntent);
        finish();
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user
        time =  String.format("%02d:%02d", hourOfDay, minute);
        updateTimestamp();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user
        date =   day + "/" + (month + 1) + "/" + year;
        updateTimestamp();
    }

    public void updateTimestamp() {
        TextView timestampView = (TextView) findViewById(R.id.textViewAddTimestamp);
        dueTimestamp = date + " " + time;
        String newText = dueTimestamp;
        timestampView.setText(newText);
    }

    public static class DatePickerFragment extends DialogFragment {

        private Activity mActivity;
        private DatePickerDialog.OnDateSetListener mListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            Activity activity= null;

            if (context instanceof Activity){
                activity=(Activity) context;
            }

            mActivity = activity;

            // This error will remind you to implement an OnTimeSetListener
            //   in your Activity if you forget
            try {
                mListener = (DatePickerDialog.OnDateSetListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnTimeSetListener");
            }
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(mActivity, mListener, year, month, day);
        }


    }

    public static class TimePickerFragment extends DialogFragment  {

        private Activity mActivity;
        private TimePickerDialog.OnTimeSetListener mListener;

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);

            Activity activity= null;

            if (context instanceof Activity){
                activity=(Activity) context;
            }

            mActivity = activity;

            // This error will remind you to implement an OnTimeSetListener
            //   in your Activity if you forget
            try {
                mListener = (TimePickerDialog.OnTimeSetListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement OnTimeSetListener");
            }
        }

        @Override
        @NonNull
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(mActivity, mListener, hour, minute,
                    DateFormat.is24HourFormat(mActivity));
        }

    }
}


