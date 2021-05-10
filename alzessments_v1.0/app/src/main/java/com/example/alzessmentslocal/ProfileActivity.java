package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {
    LoginDataBaseAdapter loginDataBaseAdapter;
    FAQScoresAdapter faqScoresAdapter;
    MOCAScoresAdapter mocaScoresAdapter;
    MMSEScoresAdapter mmseScoresAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        faqScoresAdapter = new FAQScoresAdapter(this);
        faqScoresAdapter = faqScoresAdapter.open();
        mocaScoresAdapter = new MOCAScoresAdapter(this);
        mocaScoresAdapter = mocaScoresAdapter.open();
        mmseScoresAdapter = new MMSEScoresAdapter(this);
        mmseScoresAdapter = mmseScoresAdapter.open();

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        String loginName = (preferences.getString("username", ""));

        TextView userProfile = (TextView)findViewById(R.id.textView_profileDisp);
        userProfile.setText(loginDataBaseAdapter.viewProfileData(loginName));
        prevFAQscores();
        prevMOCAscores();
        prevMMSEscores();
    }

    public void logOut_btn(View view) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("username");
        editor.commit();

        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(ProfileActivity.this,
                "Log out successful!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void prevFAQscores() {
        //get username
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        String loginName = (preferences.getString("username", ""));

        //creating text view objects to preview the previous scores
        TextView latestFAQ = (TextView)findViewById(R.id.textView_pFAQlast);
        TextView latest2FAQ = (TextView)findViewById(R.id.textView_pFAQ2last);
        TextView latest3FAQ = (TextView)findViewById(R.id.textView_pFAQ3last);

        //declare cursor to seek information on previous FAQ scores
        Cursor data = faqScoresAdapter.getDetails(loginName);

        //Working to show latest scores, gets count and work out latest 3 scores
        int faq_count = data.getCount();
        data.moveToFirst();
        //no previous scores
        if (faq_count == 0 || data == null){
            latestFAQ.setText(" ");
            latest2FAQ.setText("No\nprevious\nscores!");
            latest3FAQ.setText(" ");
            data.close();
            //latestFAQ.setText();
            //data.getInt(data.getColumnIndex("score"))
        }
        //1 previous score
        else if (faq_count == 1){
            latestFAQ.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latestFAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latestFAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latestFAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestFAQ.setTextColor(Color.RED);
            }
            latest2FAQ.setText(" ");
            latest3FAQ.setText(" ");
            data.close();
        }
        //2 previous scores
        else if (faq_count == 2){
            data.moveToFirst();
            latest2FAQ.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latest2FAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latest2FAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latest2FAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2FAQ.setTextColor(Color.RED);
            }

            data.moveToNext();
            latestFAQ.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latestFAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latestFAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latestFAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestFAQ.setTextColor(Color.RED);
            }

            latest3FAQ.setText(" ");
            data.close();
        }
        //3 previous scores
        else if (faq_count == 3){
            data.moveToFirst();
            latest3FAQ.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latest3FAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latest3FAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latest3FAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest3FAQ.setTextColor(Color.RED);
            }

            data.moveToNext();
            latest2FAQ.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latest2FAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latest2FAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latest2FAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2FAQ.setTextColor(Color.RED);
            }

            data.moveToNext();
            latestFAQ.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latestFAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latestFAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latestFAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestFAQ.setTextColor(Color.RED);
            }

            data.close();
        }
        //4+ previous scores
        else if (faq_count > 3 ){
            data.moveToLast();
            latestFAQ.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latestFAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latestFAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latestFAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestFAQ.setTextColor(Color.RED);
            }

            data.moveToPrevious();
            latest2FAQ.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latest2FAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latest2FAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latest2FAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2FAQ.setTextColor(Color.RED);
            }

            data.moveToPrevious();
            latest3FAQ.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score<9){
                latest3FAQ.setTextColor(Color.GREEN);
            }
            else if (score >=9 && score < 15){
                latest3FAQ.setTextColor(Color.YELLOW);
            }
            else if (score >= 15 && score < 25){
                latest3FAQ.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest3FAQ.setTextColor(Color.RED);
            }

            data.close();
        }
        else {
            latestFAQ.setText("Error\nloading\nscores!\n TRY AGAIN");
            latest2FAQ.setText(" ");
            latest3FAQ.setText(" ");
            data.close();
        }
    }

    public void prevMOCAscores(){
        //get username
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        String loginName = (preferences.getString("username", ""));

        //creating text view objects to preview the previous scores
        TextView latestMOCA = (TextView)findViewById(R.id.textView_pMOCAlast);
        TextView latest2MOCA = (TextView)findViewById(R.id.textView_pMOCA2last);
        TextView latest3MOCA = (TextView)findViewById(R.id.textView_pMOCA3last);

        //declare cursor to seek information on previous FAQ scores
        Cursor data = mocaScoresAdapter.getDetails(loginName);

        //Working to show latest scores, gets count and work out latest 3 scores
        int moca_count = data.getCount();
        data.moveToFirst();

        //no prev scores
        if (moca_count == 0 || data == null) {
            latestMOCA.setText(" ");
            latest2MOCA.setText("No\nprevious\nscores!");
            latest3MOCA.setText(" ");
            data.close();
        }
        //1 previous score
        else if (moca_count == 1){
            latestMOCA.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latestMOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latestMOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latestMOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMOCA.setTextColor(Color.RED);
            }
            latest2MOCA.setText(" ");
            latest3MOCA.setText(" ");
            data.close();
        }
        //2 previous scores
        else if (moca_count == 2){
            data.moveToFirst();
            latest2MOCA.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latest2MOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latest2MOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latest2MOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2MOCA.setTextColor(Color.RED);
            }

            data.moveToNext();
            latestMOCA.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latestMOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latestMOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latestMOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMOCA.setTextColor(Color.RED);
            }

            latest3MOCA.setText(" ");
            data.close();
        }
        //3 previous scores
        else if (moca_count == 3){
            data.moveToFirst();
            latest3MOCA.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latest3MOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 221){
                latest3MOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latest3MOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest3MOCA.setTextColor(Color.RED);
            }

            data.moveToNext();
            latest2MOCA.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latest2MOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latest2MOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latest2MOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2MOCA.setTextColor(Color.RED);
            }

            data.moveToNext();
            latestMOCA.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latestMOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latestMOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latestMOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMOCA.setTextColor(Color.RED);
            }

            data.close();
        }
        //4+ previous scores
        else if (moca_count > 3 ){
            data.moveToLast();
            latestMOCA.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latestMOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latestMOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latestMOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMOCA.setTextColor(Color.RED);
            }

            data.moveToPrevious();
            latest2MOCA.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latest2MOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latest2MOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latest2MOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2MOCA.setTextColor(Color.RED);
            }

            data.moveToPrevious();
            latest3MOCA.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=21){
                latest3MOCA.setTextColor(Color.GREEN);
            }
            else if (score >=14 && score < 21){
                latest3MOCA.setTextColor(Color.YELLOW);
            }
            else if (score >= 10 && score < 14){
                latest3MOCA.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest3MOCA.setTextColor(Color.RED);
            }

            data.close();
        }
        else {
            latestMOCA.setText("Error\nloading\nscores!\n TRY AGAIN");
            latest2MOCA.setText(" ");
            latest3MOCA.setText(" ");
            data.close();
        }
    }

    public void prevMMSEscores(){
        //get username
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ProfileActivity.this);
        String loginName = (preferences.getString("username", ""));

        //creating text view objects to preview the previous scores
        TextView latestMMSE = (TextView)findViewById(R.id.textView_pMMSElast);
        TextView latest2MMSE = (TextView)findViewById(R.id.textView_pMMSE2last);
        TextView latest3MMSE = (TextView)findViewById(R.id.textView_pMMSE3last);

        //declare cursor to seek information on previous FAQ scores
        Cursor data = mmseScoresAdapter.getDetails(loginName);

        //Working to show latest scores, gets count and work out latest 3 scores
        int mmse_count = data.getCount();
        data.moveToFirst();

        //no prev scores
        if (mmse_count == 0 || data == null) {
            latestMMSE.setText(" ");
            latest2MMSE.setText("No\nprevious\nscores!");
            latest3MMSE.setText(" ");
            data.close();
        }
        //1 previous score
        else if (mmse_count == 1){
            latestMMSE.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latestMMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latestMMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latestMMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMMSE.setTextColor(Color.RED);
            }
            latest2MMSE.setText(" ");
            latest3MMSE.setText(" ");
            data.close();
        }
        //2 previous scores
        else if (mmse_count == 2){
            data.moveToFirst();
            latest2MMSE.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latest2MMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latest2MMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latest2MMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2MMSE.setTextColor(Color.RED);
            }

            data.moveToNext();
            latestMMSE.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latestMMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latestMMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latestMMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMMSE.setTextColor(Color.RED);
            }

            latest3MMSE.setText(" ");
            data.close();
        }
        //3 previous scores
        else if (mmse_count == 3){
            data.moveToFirst();
            latest3MMSE.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latest3MMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latest3MMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latest3MMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest3MMSE.setTextColor(Color.RED);
            }

            data.moveToNext();
            latest2MMSE.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latest2MMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latest2MMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latest2MMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2MMSE.setTextColor(Color.RED);
            }

            data.moveToNext();
            latestMMSE.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latestMMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latestMMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latestMMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMMSE.setTextColor(Color.RED);
            }

            data.close();
        }
        //4+ previous scores
        else if (mmse_count > 3 ){
            data.moveToLast();
            latestMMSE.setText(data.getString(data.getColumnIndex("score")));
            int score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latestMMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latestMMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latestMMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latestMMSE.setTextColor(Color.RED);
            }

            data.moveToPrevious();
            latest2MMSE.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latest2MMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latest2MMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latest2MMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest2MMSE.setTextColor(Color.RED);
            }

            data.moveToPrevious();
            latest3MMSE.setText(data.getString(data.getColumnIndex("score")));
            score = Integer.parseInt(data.getString(data.getColumnIndex("score")));
            if (score>=18){
                latest3MMSE.setTextColor(Color.GREEN);
            }
            else if (score >=15 && score < 18){
                latest3MMSE.setTextColor(Color.YELLOW);
            }
            else if (score >= 11 && score < 15){
                latest3MMSE.setTextColor(Color.rgb(255,165,0));
            }
            else {
                latest3MMSE.setTextColor(Color.RED);
            }

            data.close();
        }
        else {
            latestMMSE.setText("Error\nloading\nscores!\n TRY AGAIN");
            latest2MMSE.setText(" ");
            latest3MMSE.setText(" ");
            data.close();
        }

    }
}