package com.uoit.calvin.mytodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tasksDB";
    private static final String TABLE_TASKS = "task";

    private static  final String KEY_ID = "id";
    private static final String KEY_TITLE= "title";
    private static final String KEY_TIMESTAMPS = "timestamps";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_SELECTED = "selected";
    private static final String KEY_WEATHER_CITY = "weatherCity";
    private static final String KEY_WEATHER_DETAILS = "weatherDetails";
    private static final String KEY_WEATHER_TEMPERATURE = "weatherTemperature";
    private static final String KEY_HIDDEN = "hidden";
    private static final String KEY_COMPLETED = "completed";
    private static final String KEY_DUE_TIMESTAMP = "dueTimestamp";
    private static final String KEY_DETAILS = "details";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_TITLE + " TEXT," +
                    KEY_TIMESTAMPS+ " TEXT," +
                    KEY_LATITUDE + " REAL," +
                    KEY_LONGITUDE + " REAL," +
                    KEY_SELECTED + " INTEGER," +
                    KEY_WEATHER_CITY + " TEXT," +
                    KEY_WEATHER_DETAILS + " TEXT," +
                    KEY_WEATHER_TEMPERATURE + " TEXT," +
                    KEY_HIDDEN + " REAL," +
                    KEY_COMPLETED + " REAL," +
                    KEY_DUE_TIMESTAMP + " TEXT," +
                    KEY_DETAILS + " TEXT" + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_TASKS;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean addTransactions(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, task.getTitle());
        values.put(KEY_TIMESTAMPS, task.getTimestamp());
        values.put(KEY_LATITUDE, task.getLatitude());
        values.put(KEY_LONGITUDE, task.getLongitude());
        values.put(KEY_SELECTED, task.isSelected());
        values.put(KEY_WEATHER_CITY, task.getWeather().getCity());
        values.put(KEY_WEATHER_DETAILS, task.getWeather().getDetails());
        values.put(KEY_WEATHER_TEMPERATURE, task.getWeather().getTemperature());
        values.put(KEY_HIDDEN, task.isHidden());
        values.put(KEY_COMPLETED, task.isCompleted());
        values.put(KEY_DUE_TIMESTAMP, task.getDueTimestamp());
        values.put(KEY_DETAILS, task.getDetails());
        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close();
        return true;
    }

    public void updateShow(long id, boolean show) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_HIDDEN, show);
        db.update(TABLE_TASKS, values,  KEY_ID+"="+id, null);
    }

    public void updateSelected(long id, boolean selected) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_SELECTED, selected);
        db.update(TABLE_TASKS, values,  KEY_ID+"="+id, null);
    }

    public void updateCompleted(long id, boolean completed) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(KEY_COMPLETED, completed);
        db.update(TABLE_TASKS, values,  KEY_ID+"="+id, null);
    }



    public void deleteTransactions(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = " + id, null);
        db.close();
    }


    public Task getSingleData(long id) {
        Task task = new Task();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_ID + "='" + id + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
                task = setTask(cursor);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        return task;
    }

    // Getting All Data
    public List<Task> getAllData() {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Task task = setTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return taskList;
    }



    // Getting all show data
    public List<Task> getAllShowData() {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_HIDDEN + " ='0'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Task task = setTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return taskList;
    }

    // Getting all completed data
    public List<Task> getAllCompletedData() {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_COMPLETED + " ='1'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Task task = setTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return taskList;
    }

    // Getting all incomplete data
    public List<Task> getAllIncompleteData() {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_COMPLETED + " ='0'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Task task = setTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return taskList;
    }

    // Getting all hidden data
    public List<Task> getAllHiddenData() {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS + " WHERE " + KEY_HIDDEN + " ='1'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Task task = setTask(cursor);
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return taskList;
    }

    private Task setTask(Cursor cursor) {

        Task task = new Task();

        task.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
        task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        task.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMPS)));
        task.setLatitude(cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE)));
        task.setLongitude(cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE)));
        task.setSelected(cursor.getInt(cursor.getColumnIndex(KEY_SELECTED)) == 1);

        Weather weather = new Weather();
        weather.setCity(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_CITY)));
        weather.setDetails(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_DETAILS)));
        weather.setTemperature(cursor.getString(cursor.getColumnIndex(KEY_WEATHER_TEMPERATURE)));
        task.setWeather(weather);

        task.setHidden(cursor.getInt(cursor.getColumnIndex(KEY_HIDDEN)) == 1);
        task.setCompleted(cursor.getInt(cursor.getColumnIndex(KEY_COMPLETED)) == 1);

        task.setDueTimestamp(cursor.getString(cursor.getColumnIndex(KEY_DUE_TIMESTAMP)));
        task.setDetails(cursor.getString(cursor.getColumnIndex(KEY_DETAILS)));
        return task;

    }

}
