package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class faqActivity extends AppCompatActivity {
    FAQScoresAdapter scoresAdapter;
    //questions string array for FAQ questions
    String[] questionStr = {
            "Writing checks, paying bills, balancing checkbook.",
            "Assembling tax records, business affairs, or papers.",
            "Shopping alone for clothes, household necessities, or groceries.",
            "Playing a game of skill, working a hobby.",
            "Heating water, making a cup of coffee, turning off a stove after use.",
            "Preparing a balanced meal.",
            "Keeping track of current events.",
            "Paying attention to, understanding, discussing TV, book, magazine.",
            "Remembering appointments, family occasions, holidays, medications.",
            "Travelling out of neighbourhood, driving, arranging buses."
    };

    //counters for user score and question number etc
    int questionNo = 0;
    int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);

        scoresAdapter = new FAQScoresAdapter(this);
        scoresAdapter = scoresAdapter.open();
        startFAQ();
    }

    public void startFAQ(){
        //question text view
        TextView textView_question = (TextView)findViewById(R.id.textView_faqQuestion);
        textView_question.setText(questionStr[questionNo]);
    }
    public void finishFAQ(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(faqActivity.this);
        String userName = (preferences.getString("username", ""));

        scoresAdapter.insertScore(userName, score);
        Toast.makeText(getApplicationContext(),
                "You scored " + score +". A score over 9 indicates a cognitive impairment", Toast.LENGTH_LONG).show();
        Intent intent = new Intent (faqActivity.this, FinishActivity.class);
        startActivity(intent);
        scoresAdapter.close();
    }

    //user button detect
    public void scoreAdd3(View view){
        score = score + 3;
        questionNo++;
        if(questionNo<10){
            startFAQ();
        }
        else{
            finishFAQ();
        }
    }
    public void scoreAdd2(View view){
        score = score + 2;
        questionNo++;
        if(questionNo<10){
            startFAQ();
        }
        else{
            finishFAQ();
        }
    }
    public void scoreAdd1(View view){
        score = score + 1;
        questionNo++;
        if(questionNo<10){
            startFAQ();
        }
        else{
            finishFAQ();
        }
    }
    public void scoreAdd0(View view){
        questionNo++;
        if(questionNo<10){
            startFAQ();
        }
        else{
            finishFAQ();
        }
    }

}