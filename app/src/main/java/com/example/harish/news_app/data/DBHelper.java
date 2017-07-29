package com.example.harish.news_app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by harish on 28-07-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsitems.db";
    private static final String TAG = "dbhelper";

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creating the table on initialization
    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryString = "CREATE TABLE " + Contract.NEWS_TABLE.TABLE_NAME + " ("+
                Contract.NEWS_TABLE._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Contract.NEWS_TABLE.COLUMN_NAME_DESCRIPTION + " TEXT NOT NULL, " +
                Contract.NEWS_TABLE.COLUMN_NAME_DATE + " DATE, " +
                Contract.NEWS_TABLE.COLUMN_NAME_URL + " TEXT NOT NULL, " +
                Contract.NEWS_TABLE.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                Contract.NEWS_TABLE.COLUMN_NAME_URL_TO_IMAGE + " TEXT NOT NULL, " +
                Contract.NEWS_TABLE.COLUMN_NAME_AUTHOR + " TEXT NOT NULL " + "); ";

        Log.d(TAG, "Create table SQL: " + queryString);
        db.execSQL(queryString);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
