package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import java.util.Calendar;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

public class Home extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView txtname = (TextView)findViewById(R.id.textView_user);

        //welcome message
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Home.this);
        String loginName = (preferences.getString("username", ""));


        int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        //morning
        if((currentTime>=0)&&(currentTime<12)){
            txtname.setText("Good morning, " + loginName);
        }
        //afternoon
        else if ((currentTime>=12)&&(currentTime<17)){
            txtname.setText("Good afternoon, " + loginName);
        }
        //evening
        else{
            txtname.setText("Good evening, " + loginName);
        }
    }

    public void goProfile(View view){
        Intent intent = new Intent (Home.this, ProfileActivity.class);
        startActivity(intent);
    }
    public void goFAQ(View view){
        Intent intent = new Intent (Home.this, faqActivity.class);
        startActivity(intent);
    }
    public void goMMSE(View view){
        Intent intent = new Intent (Home.this, mmseActivity.class);
        startActivity(intent);
    }
    public void goMOCA(View view){
        Intent intent = new Intent (Home.this, mocaActivity.class);
        startActivity(intent);
    }
}