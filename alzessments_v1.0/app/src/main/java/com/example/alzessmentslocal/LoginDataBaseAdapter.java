package com.example.alzessmentslocal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

public class LoginDataBaseAdapter {
    //db version
    private static final int DATABASE_VERSION = 1;
    //db name
    private static final String DATABASE_NAME = "Login.db";
    //variable to hold db instance
    private static SQLiteDatabase db;
    //db ug helper
    private DataBaseHelper dataBaseHelper;


    //constructor
    public LoginDataBaseAdapter(Context context){
        dataBaseHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    //opens database
    public LoginDataBaseAdapter open() throws SQLException{
        db = dataBaseHelper.getWritableDatabase();
        return this;
    }
    //close db
    public void close(){
        db.close();
    }
    //return instance of db
    public SQLiteDatabase getDatabaseInstance(){
        return db;
    }

    //insert username into table
    public void insertEntry(String un, String pw, String ug, String em, String dob){
        ContentValues newValues = new ContentValues();
        //give rows value
        newValues.put("userName", un);
        newValues.put("userPassword", pw);
        newValues.put("userGender", ug);
        newValues.put("userEmail", em);
        newValues.put("userDOB", dob);

        //insert this into db
        db.insert("User", null, newValues);
    }

    //get password for username
    public String getSingleEntry(String un){
        db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.query("User", null, "userName=?",
                new String[]{un}, null, null,null);
        if(cursor.getCount()<1){
            cursor.close();
            return "NOT EXIST";
        }
        cursor.moveToFirst();
        String getPassword = cursor.getString(cursor.getColumnIndex("userPassword"));
        cursor.close();
        return getPassword;
    }

    public Cursor getDetails() {
        db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select  userName , userEmail from User order by UserID",
                null);
        return cursor;
    }
    public boolean usernameSearch (String un){
        db = dataBaseHelper.getReadableDatabase();
        Cursor data = getDetails();
        while (data.moveToNext()) {
            String usernameCheck = data.getString(data.getColumnIndex("userName"));
            if(usernameCheck.equals(un)){
                data.close();
                return false;
            }
        }
        return true;
    }
    public String viewProfileData(String sessionUser){
        db = dataBaseHelper.getReadableDatabase();
        Cursor cursor = db.query("User", null, "userName=?",
                new String[]{sessionUser}, null, null,null);
        cursor.moveToFirst();
        String userEmail_ = cursor.getString(cursor.getColumnIndex("userEmail"));
        String userDOB_ = cursor.getString(cursor.getColumnIndex("userDOB"));
        cursor.close();

        String userInfo = "User: "+sessionUser+"\n\nEmail: "+userEmail_+"\n\nDate of Birth: "+userDOB_;
        return userInfo;
    }
}
