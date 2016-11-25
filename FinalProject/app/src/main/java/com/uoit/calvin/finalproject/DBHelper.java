package com.uoit.calvin.finalproject;

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

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_TASKS + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    KEY_TITLE + " TEXT," +
                    KEY_TIMESTAMPS+ " TEXT," +
                    KEY_LATITUDE + " REAL," +
                    KEY_LONGITUDE + " REAL" + " )";

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

        // Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close();
        return true;
    }

    public void deleteTransactions(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = " + id, null);
        db.close();
    }

    // Getting All Data
    public List<Task> getAllData() {
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            Task task = new Task();
            task.setId(cursor.getLong(cursor.getColumnIndex(KEY_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            task.setTimestamp(cursor.getString(cursor.getColumnIndex(KEY_TIMESTAMPS)));
            task.setLatitude(cursor.getDouble(cursor.getColumnIndex(KEY_LATITUDE)));
            task.setLongitude(cursor.getDouble(cursor.getColumnIndex(KEY_LONGITUDE)));
            taskList.add(task);
            cursor.moveToNext();
        }

        cursor.close();
        return taskList;
    }

}
