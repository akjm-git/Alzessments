package com.example.alzessmentslocal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_USER_TABLE = "create table User (userID integer primary key autoincrement, " +
                "userName text, userPassword text, userGender text, userEmail text, userDOB text);";
        try{
            sqLiteDatabase.execSQL(CREATE_USER_TABLE);
        }
        catch (Exception er){
            Log.e("Error : ", "exception");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //drop table if exists
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+"User");
        //create new one
        onCreate(sqLiteDatabase);
    }
}
