package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class FinishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
    }
    public void homeFromFinish(View view){
        Intent intent = new Intent(FinishActivity.this, Home.class);
        startActivity(intent);
        finish();
    }
}