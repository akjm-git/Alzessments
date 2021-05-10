package com.example.alzessmentslocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class MOCAScoresAdapter {
    //db version
    private static final int DATABASE_VERSION = 1;
    //db name
    private static final String DATABASE_NAME = "MOCAScores.db";
    //variable to hold db instance
    private static SQLiteDatabase db;
    //db ug helper
    private MOCAScoresHelper scoresHelper;


    //constructor
    public MOCAScoresAdapter(Context context) {
        scoresHelper = new MOCAScoresHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //opens database
    public MOCAScoresAdapter open() throws SQLException {
        db = scoresHelper.getWritableDatabase();
        return this;
    }

    //close db
    public void close() {
        db.close();
    }

    //return instance of db
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    public void insertScore(String un, int score) {
        ContentValues newValues = new ContentValues();
        //give rows value
        newValues.put("userName", un);
        newValues.put("score", score);

        //insert this into db
        db.insert("scores", null, newValues);
    }

    public Cursor getDetails(String un) {
        /*
        db = scoresHelper.getReadableDatabase();
        String query = "SELECT * FROM Scores WHERE userName LIKE '%" + un + "%'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
        */

        db = scoresHelper.getReadableDatabase();
        Cursor cursor = db.query("Scores", null, "userName=?",
                new String[]{un}, null, null,null);
        return cursor;
    }
}
