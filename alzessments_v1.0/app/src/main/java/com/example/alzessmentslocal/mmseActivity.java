package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class mmseActivity extends AppCompatActivity {
    MMSEScoresAdapter scoresAdapter;
    FusedLocationProviderClient fusedLocationProviderClient;
    String[] questionStr = {
            "section1",
            "Learn the words that will appear on the screen",
            "Serial subtraction of 7 from 100.",
            "Recall the words from earlier.",
            "Name these objects.",
            "Ask the assessee to repeat the sentence below.",
            "Read the 3 step command to the assessee.\n1 point for each stage.",
            "Ask the assessee to read and obey the command.",
            "Ask the assessee to type a fluent sentence.",
            "Ask the assessee to copy these pentagons onto a piece of paper."
    };
    int questionNo=0;
    int score=0;
    String recalledList ="";
    int section1count=0;
    int section2count=0;
    int section3count=0;
    int section4count=0;
    int section5count=0;
    int mistakeCounter=0;
    double lat;
    double longt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mmse);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mmseActivity.this);
        String userName = (preferences.getString("username", ""));
        String superV = ", in order to complete this assessment, a supervisor is required.";
        EditText input = (EditText)findViewById(R.id.editText_mmseInput);
        TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        TextView disp = (TextView)findViewById(R.id.textView_mmseDisp);
        ImageView imageDisp = (ImageView)findViewById(R.id.imageView_mmse);
        final Button btn_NO = (Button) findViewById(R.id.button_mmseNo);
        final Button btn_YES = (Button) findViewById(R.id.button_mmseYes);
        Button btn_okay = (Button)findViewById(R.id.button_mmseSub);
        scoresAdapter = new MMSEScoresAdapter(this);
        scoresAdapter = scoresAdapter.open();


        btn_YES.setText("I have a supervisor present");
        btn_NO.setText("I do not have a supervisor present");
        btn_YES.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_YES.setVisibility(View.INVISIBLE);
                btn_NO.setVisibility(View.INVISIBLE);
                SV();
            }
        });
        btn_NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_YES.setVisibility(View.INVISIBLE);
                btn_NO.setVisibility(View.INVISIBLE);
                noSV();
            }
        });

        //alert that a supervisor is needed for part of this assessment
        int currentTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        //morning
        if ((currentTime >= 0) && (currentTime < 12)) {
            question.setText("Good morning, " + userName + superV);
        }
        //afternoon
        else if ((currentTime >= 12) && (currentTime < 17)) {
            question.setText("Good afternoon, " + userName + superV);
        }
        //evening
        else {
            question.setText("Good evening, " + userName + superV);
        }
        imageDisp.setVisibility(View.INVISIBLE);
        disp.setVisibility(View.INVISIBLE);
        input.setVisibility(View.INVISIBLE);
        btn_okay.setVisibility(View.INVISIBLE);
        btn_YES.setVisibility(View.VISIBLE);
        btn_NO.setVisibility(View.VISIBLE);
    }

    //begin and finish mmse functions, adding scores to db etc
    public void startMMSE(){
        section1();
    }
    public void finishMMSE(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mmseActivity.this);
        String userName = (preferences.getString("username", ""));

        scoresAdapter.insertScore(userName, score);
        Toast.makeText(getApplicationContext(),
                "You scored " + score +". A score under 18 indicates a cognitive impairment", Toast.LENGTH_LONG).show();
        Intent intent = new Intent (mmseActivity.this, FinishActivity.class);
        startActivity(intent);
        scoresAdapter.close();
    }

    //checking for a supervisor present
    public void SV(){
        startMMSE();
    }
    public void noSV(){
        Toast.makeText(getApplicationContext(),
                "Please ensure you have a supervisor for the MMSE...", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(mmseActivity.this, Home.class);
        startActivity(intent);
    }

    public void toSupervisor(final int sectionNumber){
        final TextView disp = (TextView)findViewById(R.id.textView_mmseDisp);
        final Button btn_okay = (Button)findViewById(R.id.button_mmseSub);
        TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        question.setVisibility(View.INVISIBLE);

        disp.setVisibility(View.VISIBLE);
        disp.setTextColor(Color.WHITE);
        disp.setText("Please pass the device to your supervisor.");
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_okay.setText("Okay");
            }

            @Override
            public void onFinish() {
                btn_okay.setVisibility(View.VISIBLE);
            }
        }.start();
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp.setVisibility(View.INVISIBLE);
                btn_okay.setVisibility(View.INVISIBLE);
                if(sectionNumber == 2){
                    section3();
                }else{
                    section5();
                }
            }
        });
    }
    public void toPatient(final int sectionNumber){
        final TextView disp = (TextView)findViewById(R.id.textView_mmseDisp);
        final Button btn_okay = (Button)findViewById(R.id.button_mmseSub);

        disp.setVisibility(View.VISIBLE);
        disp.setText("Please pass the device to the assessee.");
        new CountDownTimer(4000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                btn_okay.setText("Okay");
            }

            @Override
            public void onFinish() {
                btn_okay.setVisibility(View.VISIBLE);
            }
        }.start();
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disp.setVisibility(View.INVISIBLE);
                btn_okay.setVisibility(View.INVISIBLE);
                if(sectionNumber == 3){
                    section4();
                }else{
                    section5();
                }
            }
        });
    }

    //Sections of MMSE
    public void section1(){
        TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        EditText input = (EditText)findViewById(R.id.editText_mmseInput);
        Button btn_okay = (Button)findViewById(R.id.button_mmseSub);
        String[] subQuestion = {
                "What day is it?",
                "What month is it?",
                "What year is it?",
                "",
                "Which country are you in?"
        };

        /*fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        String countryName;
        String cityName;*/



        if(section1count==0){
            question.setText(subQuestion[section1count]);
            btn_okay.setVisibility(View.VISIBLE);
            input.setVisibility(View.VISIBLE);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userInput = ((EditText)findViewById(R.id.editText_mmseInput)).getText().toString().toLowerCase();
                    recalledList = userInput;
                    section1count++;
                    section1();
                }
            });
        }else if(section1count==1) {
            question.setText(subQuestion[section1count]);

            String[] days = {"sun", "mon", "tues", "wed", "thurs", "fri", "sat"};
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (recalledList.contains(days[currentDay-1])){
                score++;
            }
            input.setText("");
        }else if (section1count==2){
            question.setText(subQuestion[section1count]);
            String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec",};
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            if (recalledList.contains(months[currentMonth])){
                score++;
            }
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setText("");
        }else if(section1count==3){
            question.setText(subQuestion[section1count]);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (recalledList.contains(String.valueOf(currentYear))||recalledList.contains(String.valueOf(currentYear-2000))){
                score++;
            }
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText("");
            input.setVisibility(View.INVISIBLE);
            questionNo++;
            section2();
        }
        /*else if(section1count==4){
            question.setText(subQuestion[section1count]);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (recalledList.contains(String.valueOf(currentYear))){
                score++;
                Toast.makeText(this, "Hola3", Toast.LENGTH_LONG).show();
            }
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText("");
        }else if(section1count==5){
            question.setText(subQuestion[section1count]);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (recalledList.contains(String.valueOf(currentYear))){
                score++;
                Toast.makeText(this, "Hola3", Toast.LENGTH_LONG).show();
            }
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText("");
        }*/
    }
    public void section2(){
        TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        question.setText(questionStr[questionNo]);
        final TextView disp = (TextView) findViewById(R.id.textView_mmseDisp);
        final String[] words = {"Apple", "Table", "Penny"};
        final EditText input = (EditText) findViewById(R.id.editText_mmseInput);
        Button btn_okay = (Button)findViewById(R.id.button_mmseSub);

        //timer for showing words to user for 5 seconds each
        if (section2count < 3) {
            question.setVisibility(View.VISIBLE);
            disp.setVisibility(View.VISIBLE);
            btn_okay.setVisibility(View.INVISIBLE);
            new CountDownTimer(2500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    disp.setText(words[section2count]);
                    disp.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    disp.setVisibility(View.INVISIBLE);
                    section2count++;
                    section2();
                }
            }.start();
        } else if (section2count>=3&&section2count<6) {
            disp.setText("Please repeat the words...");
            input.setText("");
            disp.setTextColor(Color.WHITE);
            disp.setVisibility(View.VISIBLE);
            input.setVisibility(View.VISIBLE);
            btn_okay.setVisibility(View.VISIBLE);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputData = ((EditText)findViewById(R.id.editText_mmseInput)).getText().toString().toLowerCase();
                    recalledList = recalledList+inputData;
                    section2count++;
                    section2();
                }
            });
        } else {
            btn_okay.setVisibility(View.INVISIBLE);
            input.setVisibility(View.INVISIBLE);
            //wordRecall.setText(recalledList);
            if (recalledList.contains("apple")&&recalledList.contains("table")&&recalledList.contains("penny")) {
                new CountDownTimer(3500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        disp.setTextColor(Color.GREEN);
                        disp.setText("Correct. Please remember these words.");
                    }

                    @Override
                    public void onFinish() {
                        disp.setVisibility(View.INVISIBLE);
                        questionNo++;
                        score=(score+3);
                        toSupervisor(2);
                    }
                }.start();
            } else {
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        disp.setTextColor(Color.RED);
                        disp.setText("Incorrect. Try again.");
                        section2count = 0;
                    }

                    @Override
                    public void onFinish() {
                        disp.setTextColor(Color.WHITE);
                        recalledList = "";
                        section2();
                    }
                }.start();
            }
        }
    }
    public void section3(){
        final String[] serialSub = {"93","86","79","72","65"};
        final EditText input = (EditText)findViewById(R.id.editText_mmseInput);
        TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        TextView disp = (TextView)findViewById(R.id.textView_mmseDisp);
        Button btn_NO = (Button) findViewById(R.id.button_mmseNo);
        Button btn_YES = (Button) findViewById(R.id.button_mmseYes);
        Button btn_okay = (Button)findViewById(R.id.button_mmseSub);

        if (section3count==0) {
            //preparing UI clearing non necessary buttons text etc
            question.setText(questionStr[questionNo]);
            question.setVisibility(View.VISIBLE);
            btn_YES.setVisibility(View.INVISIBLE);
            btn_NO.setVisibility(View.INVISIBLE);
            input.setVisibility(View.INVISIBLE);
            disp.setVisibility(View.VISIBLE);
            disp.setText("Ask the assessee to complete the task outlined above.");
            btn_okay.setVisibility(View.VISIBLE);
            btn_okay.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    section3count++;
                    section3();
                }
            });
        } else if(section3count==1){
            btn_YES.setText("Assessee correct");
            btn_NO.setText("Assessee incorrect");
            btn_YES.setVisibility(View.VISIBLE);
            btn_NO.setVisibility(View.VISIBLE);
            btn_okay.setVisibility(View.INVISIBLE);

            disp.setText("1st answer:\n"+ serialSub[0]);
            btn_YES.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    section3count++;
                    section3();
                }
            });
            btn_NO.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    mistakeCounter++;
                    section3count++;
                    section3();
                }
            });

        } else if (section3count>1&&section3count<6) {
            disp.setText("Next answer:\n"+serialSub[section3count-1]);
            disp.setOnClickListener(null);
            btn_YES.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    section3count++;
                    section3();
                }
            });
            btn_NO.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    mistakeCounter++;
                    section3count++;
                    section3();
                }
            });

        } else {
            btn_YES.setVisibility(View.INVISIBLE);
            btn_NO.setVisibility(View.INVISIBLE);
            disp.setText(" ");
            input.setText("");
            score=score+(5-mistakeCounter);
            questionNo++;
            toPatient(3);
        }

    }
    public void section4(){
        TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        question.setText(questionStr[questionNo]);
        EditText input = (EditText)findViewById(R.id.editText_mmseInput);
        Button btn_okay = (Button)findViewById(R.id.button_mmseSub);
        input.setVisibility(View.VISIBLE);
        btn_okay.setVisibility(View.VISIBLE);

        if (section4count==0){
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputData = ((EditText)findViewById(R.id.editText_mmseInput)).getText().toString().toLowerCase();
                    recalledList = recalledList+inputData;
                    section4count++;
                    section4();
                }
            });
        }
        else if (section4count<3){
            input.setText("");
        }
        else{
            if (recalledList.contains("apple")){
                score++;
            }
            if (recalledList.contains("table")){
                score++;
            }
            if (recalledList.contains("penny")){
                score++;
            }

            questionNo++;
            input.setVisibility(View.INVISIBLE);
            input.setText("");
            btn_okay.setVisibility(View.INVISIBLE);
            recalledList="";
            section5();
        }
    }
    public void section5(){
        final TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        final EditText input = (EditText)findViewById(R.id.editText_mmseInput);
        final TextView disp = (TextView)findViewById(R.id.textView_mmseDisp);
        final Button btn_yes = (Button)findViewById(R.id.button_mmseYes);
        final Button btn_no = (Button)findViewById(R.id.button_mmseNo);
        final ImageView images = (ImageView)findViewById(R.id.imageView_mmse);
        final Button btn_okay = (Button)findViewById(R.id.button_mmseSub);
        question.setText(questionStr[questionNo]);
        question.setVisibility(View.VISIBLE);

        //naming objects
        if (section5count<2){
            input.setVisibility(View.VISIBLE);
            images.setVisibility(View.VISIBLE);
            btn_okay.setVisibility(View.VISIBLE);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText input = (EditText)findViewById(R.id.editText_mmseInput);
                    recalledList=recalledList+(input.getText().toString().toLowerCase());
                    if(section5count==1){
                        if (recalledList.contains("pencil")){
                            score++;
                        }
                        if (recalledList.contains("watch")){
                            score++;
                        }
                        section5count++;
                        input.setVisibility(View.INVISIBLE);
                        images.setVisibility(View.INVISIBLE);
                        btn_okay.setVisibility(View.INVISIBLE);
                        questionNo++;
                        toSupervisor(5);
                    }else{
                        input.setText("");
                        images.setImageResource(R.drawable.mmse_watch);
                        section5count++;
                    }
                }
            });
        } else if(section5count==2){ //language
            String sentence="No ifs, ands, or buts";
            question.setText(questionStr[questionNo]);
            disp.setText(sentence);
            disp.setVisibility(View.VISIBLE);
            btn_yes.setText("Fluent");
            btn_no.setText("Not\nfluent");
            btn_yes.setVisibility(View.VISIBLE);
            btn_no.setVisibility(View.VISIBLE);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_yes.setVisibility(View.INVISIBLE);
                    btn_no.setVisibility(View.INVISIBLE);
                    disp.setText("");
                    section5count++;
                    questionNo++;
                    score++;
                    section5();
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_yes.setVisibility(View.INVISIBLE);
                    btn_no.setVisibility(View.INVISIBLE);
                    disp.setText("");
                    section5count++;
                    questionNo++;
                    section5();
                }
            });
        }else if (section5count==3){//3 stage command
            disp.setText("Place index finger of right hand\n on your nose,\n and then on your left ear");
            input.setHint("stages correct...");
            input.setText("");
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setVisibility(View.VISIBLE);
            btn_okay.setVisibility(View.VISIBLE);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userInput = ((EditText)findViewById(R.id.editText_mmseInput)).getText().toString();
                    if(userInput.equals("")){
                        input.setError("Score must be from 0 to 3!");
                    }else{
                        int inputData  = Integer.parseInt(((EditText)findViewById(R.id.editText_mmseInput)).getText().toString());
                        if(inputData>3||inputData<0){
                            input.setError("Score must be from 0 to 3!");
                            input.setText("");
                        }else{
                            section5count++;
                            score = score + inputData;
                            questionNo++;
                            input.setText("");
                            section5();
                        }
                    }
                }
            });
        }else if(section5count==4){
            disp.setText("Close your eyes.");
            btn_yes.setText("Successful");
            btn_no.setText("Unsuccessful");
            input.setVisibility(View.INVISIBLE);
            btn_okay.setVisibility(View.INVISIBLE);
            btn_yes.setVisibility(View.VISIBLE);
            btn_no.setVisibility(View.VISIBLE);
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    score++;
                    questionNo++;
                    section5count++;
                    section5();
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionNo++;
                    section5count++;
                    section5();
                }
            });
        }else{
            btn_yes.setVisibility(View.INVISIBLE);
            btn_no.setVisibility(View.INVISIBLE);

            if(section5count==5){
                disp.setText("");
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        section5count++;
                        btn_okay.setVisibility(View.INVISIBLE);
                        toPatient(5);
                    }
                });
                btn_okay.setVisibility(View.VISIBLE);
            }else if (section5count==6){
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setHint("type here...");
                question.setText("");
                disp.setText("Please type a fluent sentence.");
                btn_okay.setText("Submit");
                btn_okay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String sentence = ((EditText)findViewById(R.id.editText_mmseInput)).getText().toString();
                        recalledList = sentence;
                        section5count++;
                        input.setVisibility(View.INVISIBLE);
                        btn_okay.setVisibility(View.INVISIBLE);
                        toSupervisor(5);
                    }
                });
                input.setVisibility(View.VISIBLE);
                disp.setVisibility(View.VISIBLE);
                btn_okay.setVisibility(View.VISIBLE);
            }else{
                disp.setText(recalledList);
                btn_yes.setText("Yes");
                btn_no.setText("No");

                btn_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        score++;
                        questionNo++;
                        btn_yes.setVisibility(View.INVISIBLE);
                        btn_no.setVisibility(View.INVISIBLE);
                        disp.setVisibility(View.INVISIBLE);
                        section6();
                    }
                });
                btn_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_yes.setVisibility(View.INVISIBLE);
                        btn_no.setVisibility(View.INVISIBLE);
                        disp.setVisibility(View.INVISIBLE);
                        questionNo++;
                        section6();
                    }
                });
                question.setText("Is this sentence fluent?");
                btn_okay.setVisibility(View.INVISIBLE);
                input.setVisibility(View.INVISIBLE);
                btn_yes.setVisibility(View.VISIBLE);
                btn_no.setVisibility(View.VISIBLE);
                disp.setVisibility(View.VISIBLE);
            }
        }
    }
    public void section6(){
        TextView question = (TextView)findViewById(R.id.textView_mmseQuestion);
        question.setText(questionStr[questionNo]);
        final Button btn_yes = (Button)findViewById(R.id.button_mmseNo);
        final Button btn_no = (Button)findViewById(R.id.button_mmseYes);
        final ImageView image = (ImageView)findViewById(R.id.imageView_mmse);
        image.setImageResource(R.drawable.mmse_pentagon);
        image.setVisibility(View.VISIBLE);

        btn_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                score++;
                btn_yes.setVisibility(View.INVISIBLE);
                btn_no.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                finishMMSE();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_yes.setVisibility(View.INVISIBLE);
                btn_no.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
                finishMMSE();
            }
        });
        btn_yes.setText("Successful");
        btn_no.setText("Unsuccessful");
        new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                btn_yes.setVisibility(View.VISIBLE);
                btn_no.setVisibility(View.VISIBLE);
            }
        }.start();
    }
}