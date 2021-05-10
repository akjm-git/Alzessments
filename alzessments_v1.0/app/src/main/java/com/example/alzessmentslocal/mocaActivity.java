package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;

public class mocaActivity extends AppCompatActivity {
    MOCAScoresAdapter scoresAdapter;
    FusedLocationProviderClient fusedLocationProviderClient;
    //questions string array for FAQ questions
    String[] questionStr = {
            //visuospatial section 1
            "Tap the dots in the correct order, following the sequence.",
            "Ask the assessee to copy this cube onto a piece of paper.",
            "Ask the assessee to draw a clock at ten past eleven on a piece of paper.",
            //naming section 2
            "Please identify the animal below.",
            "Please identify the animal below.",
            "Please identify the animal below.",
            //memory section 3
            "Remember the words that appear on the screen.",
            //attention section 4
            "Numbers will appear on the screen. Recall these numbers in the CORRECT order.",
            "Numbers will appear on the screen. Recall these numbers in REVERSE order.",
            "A list of letters will be read aloud. Tap AUDIBLY when the letter 'A' is called.",
            "Please read the following letters aloud ensuring the assessee taps only for the letter 'A'...",
            " ",
            "Serial subtraction of 7 from 100.\n(Five subtractions)",
            //language section 5
            "Ask the assessee to repeat the following sentences...",
            "Count the number of words the assessee can name beginning with 'F' in one minute.",
            //abstraction section 6
            "Describe the similarity between these items.",
            //Delayed recall section 7
            "Recall the five words you were asked to remember earlier in the assessment.",
            //orientation section 8
            "What is today's date?",
            "What is the month?",
            "What is the year?",
            " ",
            " "
    };

    //counters for user score and question number etc
    /*
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        CHANGE WHEN ADDING 1st
             questions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
    */

    String check="";
    int questionNo = 0;
    int score = 0;
    int section1_5count=0;
    int section3count = 0;
    int sec4count = 0;
    int section5count = 0;
    int section6count = 0;
    int section7count = 0;
    int section8count=0;
    String recalledList = "";
    int mistakeCounter=0;
    int timerSec5=60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moca);
        scoresAdapter = new MOCAScoresAdapter(this);
        scoresAdapter = scoresAdapter.open();

        //declaring and hiding all pieces for assessment
        ImageView moca1 = (ImageView)findViewById(R.id.imageView_1);
        ImageView moca2 = (ImageView)findViewById(R.id.imageView_2);
        ImageView moca3 = (ImageView)findViewById(R.id.imageView_3);
        ImageView moca4 = (ImageView)findViewById(R.id.imageView_4);
        ImageView moca5 = (ImageView)findViewById(R.id.imageView_5);
        ImageView mocaA = (ImageView)findViewById(R.id.imageView_a);
        ImageView mocaB = (ImageView)findViewById(R.id.imageView_b);
        ImageView mocaC = (ImageView)findViewById(R.id.imageView_c);
        ImageView mocaD = (ImageView)findViewById(R.id.imageView_d);
        ImageView mocaE = (ImageView)findViewById(R.id.imageView_e);
        moca1.setVisibility(View.INVISIBLE);
        moca2.setVisibility(View.INVISIBLE);
        moca3.setVisibility(View.INVISIBLE);
        moca4.setVisibility(View.INVISIBLE);
        moca5.setVisibility(View.INVISIBLE);
        mocaA.setVisibility(View.INVISIBLE);
        mocaB.setVisibility(View.INVISIBLE);
        mocaC.setVisibility(View.INVISIBLE);
        mocaD.setVisibility(View.INVISIBLE);
        mocaE.setVisibility(View.INVISIBLE);
        TextView dotBegin = (TextView)findViewById(R.id.textView_begin);
        dotBegin.setVisibility(View.INVISIBLE);
        TextView dotEnd = (TextView)findViewById(R.id.textView_end);
        dotEnd.setVisibility(View.INVISIBLE);
        ImageView arrow1 = (ImageView)findViewById(R.id.imageView_arrow1);
        ImageView arrow2 = (ImageView)findViewById(R.id.imageView_arrow2);
        arrow1.setVisibility(View.INVISIBLE);
        arrow2.setVisibility(View.INVISIBLE);
        Button btn_subRecall = (Button) findViewById(R.id.button_submitRecall);
        btn_subRecall.setVisibility(View.INVISIBLE);
        EditText recallAns = (EditText) findViewById(R.id.editText_mocaRecall);
        recallAns.setVisibility(View.INVISIBLE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mocaActivity.this);
        String userName = (preferences.getString("username", ""));
        EditText animalAns = (EditText) findViewById(R.id.editText_mocaAnimal);
        animalAns.setVisibility(View.INVISIBLE);
        ImageView animals = (ImageView) findViewById(R.id.imageView_animals);
        animals.setVisibility(View.INVISIBLE);
        Button btn_submit = (Button) findViewById(R.id.btn_mocaSubmitAnimal);
        btn_submit.setVisibility(View.INVISIBLE);
        TextView question = (TextView) findViewById(R.id.textView_mocaQuestion);
        Button langYes = (Button)findViewById(R.id.button_mocaLangYES);
        langYes.setVisibility(View.INVISIBLE);
        Button langNo = (Button)findViewById(R.id.button_mocaLangNO);
        langNo.setVisibility(View.INVISIBLE);

        String superV = ", in order to complete this assessment, a supervisor is required.";

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
    }

    public void startMOCA() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mocaActivity.this);
        String userName = (preferences.getString("username", ""));
        Button btn_agree = (Button) findViewById(R.id.button_mocaAgreeSV);
        Button btn_disagree = (Button) findViewById(R.id.button_mocaDisagreeSV);
        btn_agree.setVisibility(View.INVISIBLE);
        btn_disagree.setVisibility(View.INVISIBLE);
        TextView question = (TextView) findViewById(R.id.textView_mocaQuestion);
        question.setText(questionStr[questionNo]);
        section1();
    }

    public void finishMOCA() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(mocaActivity.this);
        String userName = (preferences.getString("username", ""));

        scoresAdapter.insertScore(userName, score);
        Toast.makeText(getApplicationContext(),
                "You scored " + score + ". A score under 21 indicates a cognitive impairment", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(mocaActivity.this, FinishActivity.class);
        startActivity(intent);
        scoresAdapter.close();
    }

    //no supervisor goes back to main menu
    public void noSV(View view) {
        if (sec4count==0){
            Toast.makeText(getApplicationContext(),
                    "Please ensure you have a supervisor for the MoCA...", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(mocaActivity.this, Home.class);
            startActivity(intent);
        }
        else if (sec4count<54){
            sec4count++;
            score++;
            section4();
        }
    }

    //supervisor proceeds with moca assessment
    public void SV(View view) {
        if (sec4count==0){
            startMOCA();
        }
        else if (sec4count<54){
            sec4count++;
            section4();
        }
    }

    //Each section separate for clarity
    public void section1() {
        final ImageView moca1 = (ImageView)findViewById(R.id.imageView_1);
        final ImageView mocaA = (ImageView)findViewById(R.id.imageView_a);
        final ImageView moca2 = (ImageView)findViewById(R.id.imageView_2);
        moca1.setImageResource(R.drawable.moca_r1);
        mocaA.setImageResource(R.drawable.moca_ra);
        moca2.setImageResource(R.drawable.moca_r2);
        moca1.setClickable(false);
        mocaA.setClickable(false);
        moca2.setClickable(false);

        final ImageView moca3 = (ImageView)findViewById(R.id.imageView_3);
        final ImageView moca4 = (ImageView)findViewById(R.id.imageView_4);
        final ImageView moca5 = (ImageView)findViewById(R.id.imageView_5);
        final ImageView mocaB = (ImageView)findViewById(R.id.imageView_b);
        final ImageView mocaC = (ImageView)findViewById(R.id.imageView_c);
        final ImageView mocaD = (ImageView)findViewById(R.id.imageView_d);
        final ImageView mocaE = (ImageView)findViewById(R.id.imageView_e);
        moca1.setVisibility(View.VISIBLE);
        moca2.setVisibility(View.VISIBLE);
        moca3.setVisibility(View.VISIBLE);
        moca4.setVisibility(View.VISIBLE);
        moca5.setVisibility(View.VISIBLE);
        mocaA.setVisibility(View.VISIBLE);
        mocaB.setVisibility(View.VISIBLE);
        mocaC.setVisibility(View.VISIBLE);
        mocaD.setVisibility(View.VISIBLE);
        mocaE.setVisibility(View.VISIBLE);
        final TextView dotBegin = (TextView)findViewById(R.id.textView_begin);
        final TextView dotEnd = (TextView)findViewById(R.id.textView_end);
        dotBegin.setTextColor(Color.WHITE);
        dotEnd.setTextColor(Color.WHITE);
        dotBegin.setVisibility(View.VISIBLE);
        dotEnd.setVisibility(View.VISIBLE);
        final ImageView arrow1 = (ImageView)findViewById(R.id.imageView_arrow1);
        final ImageView arrow2 = (ImageView)findViewById(R.id.imageView_arrow2);
        arrow1.setVisibility(View.VISIBLE);
        arrow2.setVisibility(View.VISIBLE);

        //numbers
        moca3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moca3.setImageResource(R.drawable.moca_r3);
                moca3.setClickable(false);
                check = (check +" 3");
            }
        });
        moca4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moca4.setImageResource(R.drawable.moca_r4);
                moca4.setClickable(false);
                check = (check +" 4");
            }
        });
        moca5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moca5.setImageResource(R.drawable.moca_r5);
                moca5.setClickable(false);
                check = (check +" 5");
            }
        });

        //letters
        mocaB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mocaB.setImageResource(R.drawable.moca_rb);
                mocaB.setClickable(false);
                check = (check +" B");
            }
        });
        mocaC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mocaC.setImageResource(R.drawable.moca_rc);
                mocaC.setClickable(false);
                check = (check +" C");
            }
        });
        mocaD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mocaD.setImageResource(R.drawable.moca_rd);
                mocaD.setClickable(false);
                check = (check +" D");
            }
        });
        mocaE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mocaE.setImageResource(R.drawable.moca_re);
                mocaE.setClickable(false);
                check = (check +" E");
                if (check.equals(" B 3 C 4 D 5 E")){
                    score++;
                    moca1.setVisibility(View.INVISIBLE);
                    moca2.setVisibility(View.INVISIBLE);
                    moca3.setVisibility(View.INVISIBLE);
                    moca4.setVisibility(View.INVISIBLE);
                    moca5.setVisibility(View.INVISIBLE);
                    mocaA.setVisibility(View.INVISIBLE);
                    mocaB.setVisibility(View.INVISIBLE);
                    mocaC.setVisibility(View.INVISIBLE);
                    mocaD.setVisibility(View.INVISIBLE);
                    mocaE.setVisibility(View.INVISIBLE);
                    arrow1.setVisibility(View.INVISIBLE);
                    arrow2.setVisibility(View.INVISIBLE);
                    dotBegin.setVisibility(View.INVISIBLE);
                    dotEnd.setVisibility(View.INVISIBLE);
                    questionNo++;
                    section1_5();
                }
                else{
                    moca1.setVisibility(View.INVISIBLE);
                    moca2.setVisibility(View.INVISIBLE);
                    moca3.setVisibility(View.INVISIBLE);
                    moca4.setVisibility(View.INVISIBLE);
                    moca5.setVisibility(View.INVISIBLE);
                    mocaA.setVisibility(View.INVISIBLE);
                    mocaB.setVisibility(View.INVISIBLE);
                    mocaC.setVisibility(View.INVISIBLE);
                    mocaD.setVisibility(View.INVISIBLE);
                    mocaE.setVisibility(View.INVISIBLE);
                    arrow1.setVisibility(View.INVISIBLE);
                    arrow2.setVisibility(View.INVISIBLE);
                    dotBegin.setVisibility(View.INVISIBLE);
                    dotEnd.setVisibility(View.INVISIBLE);
                    questionNo++;
                    section1_5();
                }
            }
        });
    }
    public void section1_5(){
        TextView question = (TextView)findViewById(R.id.textView_mocaQuestion);
        question.setText(questionStr[questionNo]);
        final Button btn_yes = (Button)findViewById(R.id.button_mocaAgreeSV);
        final Button btn_no = (Button)findViewById(R.id.button_mocaDisagreeSV);
        final ImageView image = (ImageView)findViewById(R.id.imageView_animals);
        if (section1_5count==0){
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    score++;
                    questionNo++;
                    section1_5count++;
                    btn_yes.setVisibility(View.INVISIBLE);
                    btn_no.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.INVISIBLE);
                    section1_5();
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionNo++;
                    section1_5count++;
                    btn_yes.setVisibility(View.INVISIBLE);
                    btn_no.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.INVISIBLE);
                    section1_5();
                }
            });
            btn_yes.setText("Successful");
            btn_no.setText("Unsuccessful");
            image.setImageResource(R.drawable.cube);
            new CountDownTimer(3500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    image.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    btn_yes.setVisibility(View.VISIBLE);
                    btn_no.setVisibility(View.VISIBLE);

                }
            }.start();
        }
        else{
            btn_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_yes.setVisibility(View.INVISIBLE);
                    btn_no.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.INVISIBLE);
                    score=score+3;
                    questionNo++;
                    section2();
                }
            });
            btn_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btn_yes.setVisibility(View.INVISIBLE);
                    btn_no.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.INVISIBLE);
                    questionNo++;
                    section2();
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

    public void btnSubmit(View view) {
        TextView question = (TextView) findViewById(R.id.textView_mocaQuestion);
        if (questionNo == 0) {
            questionNo++;
            question.setText(questionStr[questionNo]);
            section1();
        } else if (questionNo == 1) {
            questionNo++;
            question.setText(questionStr[questionNo]);
            section1();
        } else if (questionNo == 2) {
            questionNo++;
            question.setText(questionStr[questionNo]);
            section1();
        } else if (questionNo >= 3) {
            String userAnswer = ((EditText) findViewById(R.id.editText_mocaAnimal)).getText().toString().toLowerCase();
            if (questionNo == 3) {
                if (userAnswer.equals("lion")) {
                    questionNo++;
                    score++;
                    section2();
                } else {
                    questionNo++;
                    section2();
                }
            } else if (questionNo == 4) {
                if (userAnswer.equals("rhino") || userAnswer.equals("rhinoceros")) {
                    questionNo++;
                    score++;
                    section2();
                } else {
                    questionNo++;
                    section2();
                }
            } else if (questionNo == 5) {
                if (userAnswer.equals("camel")) {
                    questionNo++;
                    score++;
                    section3();
                } else {
                    questionNo++;
                    section3();
                }
            }
        }
    }


    public void section2() {
        ImageView animals = (ImageView) findViewById(R.id.imageView_animals);
        Button btn_submit = (Button) findViewById(R.id.btn_mocaSubmitAnimal);
        EditText animalAns = (EditText) findViewById(R.id.editText_mocaAnimal);
        animalAns.setVisibility(View.VISIBLE);
        animalAns.setText("");

        Button btn_ok = (Button) findViewById(R.id.button_mocaDisagreeSV);
        btn_ok.setVisibility(View.VISIBLE);

        //test
        TextView question = (TextView) findViewById(R.id.textView_mocaQuestion);
        question.setText(questionStr[questionNo]);

        btn_ok.setVisibility(View.INVISIBLE);
        animals.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.VISIBLE);
        btn_submit.setText("Submit");
        if (questionNo == 3) {
            animals.setImageResource(R.drawable.moca_an1);
        } else if (questionNo == 4) {
            animals.setImageResource(R.drawable.moca_an2);
        } else if (questionNo == 5) {
            animals.setImageResource(R.drawable.moca_an3);
        }
    }

    public void section3() {
        Button btn_subRecall = (Button) findViewById(R.id.button_submitRecall);
        ImageView animals = (ImageView) findViewById(R.id.imageView_animals);
        animals.setVisibility(View.INVISIBLE);
        EditText animalAns = (EditText) findViewById(R.id.editText_mocaAnimal);
        animalAns.setVisibility(View.INVISIBLE);
        Button btn_submit = (Button) findViewById(R.id.btn_mocaSubmitAnimal);
        btn_submit.setVisibility(View.INVISIBLE);
        TextView question = (TextView) findViewById(R.id.textView_mocaQuestion);
        question.setText(questionStr[questionNo]);

        final TextView wordRecall = (TextView) findViewById(R.id.textView_mocaRecall);
        final String[] words = {"Face", "Velvet", "Church", "Daisy", "Red"};
        EditText recallAns = (EditText) findViewById(R.id.editText_mocaRecall);

        //timer for showing words to user for 5 seconds each
        if (section3count < 5) {
            new CountDownTimer(2500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    wordRecall.setText(words[section3count]);
                    wordRecall.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFinish() {
                    wordRecall.setVisibility(View.INVISIBLE);
                    section3count++;
                    section3();
                }
            }.start();
        } else if (section3count > 4 && section3count < 10) {
            wordRecall.setText("Please repeat the words...");
            wordRecall.setTextColor(Color.WHITE);
            wordRecall.setVisibility(View.VISIBLE);
            recallAns.setVisibility(View.VISIBLE);
            btn_subRecall.setVisibility(View.VISIBLE);
            section3count++;
        } else {
            btn_subRecall.setVisibility(View.INVISIBLE);
            recallAns.setVisibility(View.INVISIBLE);
            //wordRecall.setText(recalledList);
            if (recalledList.equals(" face velvet church daisy red")) {
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        wordRecall.setTextColor(Color.GREEN);
                        wordRecall.setText("Correct. Please remember these words.");
                    }

                    @Override
                    public void onFinish() {
                        wordRecall.setText("");
                        questionNo++;
                        section4();
                    }
                }.start();
            } else {
                new CountDownTimer(500, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        wordRecall.setTextColor(Color.RED);
                        wordRecall.setText("Incorrect. Try again.");
                        section3count = 0;
                    }

                    @Override
                    public void onFinish() {
                        wordRecall.setTextColor(Color.WHITE);
                        recalledList = "";
                        section3();
                    }
                }.start();
            }
        }
    }

    public void submitRecall1(View view) {
        //section 3
        if (sec4count == 0) {
            EditText recallAns = (EditText) findViewById(R.id.editText_mocaRecall);
            String input = ((EditText) findViewById(R.id.editText_mocaRecall)).getText().toString().toLowerCase();
            recalledList = (recalledList + " " + input);
            recallAns.setText("");
            section3();
        }
        //section 4 functions
        else if (sec4count<21) {

            EditText recallAns = (EditText) findViewById(R.id.editText_mocaRecall);
            String input = ((EditText) findViewById(R.id.editText_mocaRecall)).getText().toString().toLowerCase();
            recalledList = (recalledList + " " + input);
            recallAns.setText("");
            section4();
        }

        else{
            int x=0;
            Toast.makeText(getApplicationContext(), "TESTER tester 123", Toast.LENGTH_LONG).show();
        }

    }

    public void section4() {

        //declaring strings for the user to see and the supervisor to read.
        final String[] forwardNum = {"2", "1", "8", "5", "4"};
        final String[] backwardNum = {"7", "4", "2"};
        final String[] letterList = {"F","B","A","C","M","N","A","A","J","K","L","B","A","F","A","K","D","E","A","A","A","J","A","M","O","F","A","A","B"};
        final String[] serialSub = {"93","86","79","72","65"};
        final TextView numberRecall = (TextView) findViewById(R.id.textView_mocaRecall);
        numberRecall.setVisibility(View.VISIBLE);
        TextView question = (TextView) findViewById(R.id.textView_mocaQuestion);
        question.setText(questionStr[questionNo]);
        final EditText recallNumInput = (EditText) findViewById(R.id.editText_mocaRecall);
        recallNumInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        final Button subRecall = (Button) findViewById(R.id.button_submitRecall);

        //Forward number recall
        //give the user time to read the question 6seconds, can be increased if needed.../*\*/*\*/*\
        if (sec4count == 0) {
            new CountDownTimer(6000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    numberRecall.setTextColor(Color.WHITE);
                }

                @Override
                public void onFinish() {
                    sec4count++;
                    section4();
                }
            }.start();
        } else if (sec4count < 6) {
            //timer set number on number recall textview
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    numberRecall.setTextColor(Color.WHITE);
                    numberRecall.setText(forwardNum[sec4count - 1]);
                }

                @Override
                public void onFinish() {
                    sec4count++;
                    recalledList = "";
                    section4();
                }
            }.start();
        } else if (sec4count<12){
            numberRecall.setVisibility(View.INVISIBLE);
            //disp user input
            recallNumInput.setVisibility(View.VISIBLE);
            subRecall.setVisibility(View.VISIBLE);

            if (sec4count == 11) {
                if (recalledList.equals(" 2 1 8 5 4")){
                    numberRecall.setText("");
                    numberRecall.setVisibility(View.VISIBLE);
                    recallNumInput.setVisibility(View.INVISIBLE);
                    subRecall.setVisibility(View.INVISIBLE);
                    score++;
                    sec4count++;
                    section4();
                } else{
                    sec4count++;
                    numberRecall.setText("");
                    numberRecall.setVisibility(View.VISIBLE);
                    recallNumInput.setVisibility(View.INVISIBLE);
                    subRecall.setVisibility(View.INVISIBLE);
                    section4();
                }
            } else {
                numberRecall.setText("");
                numberRecall.setVisibility(View.VISIBLE);
                recallNumInput.setVisibility(View.VISIBLE);
                subRecall.setVisibility(View.VISIBLE);
                }
            sec4count++;
        }

        //Reverse numbers
        //Timer for user to read the question
        else if (sec4count == 12){
            questionNo++;
            question.setText(questionStr[questionNo]);
            new CountDownTimer(6000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    numberRecall.setTextColor(Color.WHITE);
                }

                @Override
                public void onFinish() {
                    sec4count++;
                    recalledList = "";
                    section4();
                }
            }.start();


        }
        else if (sec4count > 12&&sec4count<17){
            //timer set number on number recall textview
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    numberRecall.setTextColor(Color.WHITE);
                    numberRecall.setText(backwardNum[sec4count-14]);
                }

                @Override
                public void onFinish() {
                    sec4count++;
                    recalledList = "";
                    section4();
                }
            }.start();


        }
        else if (sec4count>16&&sec4count<21){
            numberRecall.setVisibility(View.INVISIBLE);
            recallNumInput.setVisibility(View.VISIBLE);
            subRecall.setVisibility(View.VISIBLE);
            if (sec4count == 20) {
                if (recalledList.equals(" 2 4 7")){
                    numberRecall.setText("");
                    numberRecall.setVisibility(View.INVISIBLE);
                    recallNumInput.setVisibility(View.INVISIBLE);
                    subRecall.setText("Okay");
                    score++;
                    sec4count++;
                    section4();
                }
                else{
                    numberRecall.setText("");
                    numberRecall.setVisibility(View.INVISIBLE);
                    recallNumInput.setVisibility(View.INVISIBLE);
                    subRecall.setText("Okay");
                    sec4count++;
                    section4();
                }
            }
            else {
                numberRecall.setText("");
                numberRecall.setVisibility(View.VISIBLE);
                recallNumInput.setVisibility(View.VISIBLE);
                subRecall.setVisibility(View.VISIBLE);
            }
            sec4count++;
        }

        //Letters section
        //Gtting user to pass device to user, 2 okay button receipts to ensure the supervisor gets access to the device.
        else if (sec4count==21){
            questionNo++;
            question.setText(questionStr[questionNo]);
            numberRecall.setTextColor(Color.WHITE);
            new CountDownTimer(6000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    subRecall.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onFinish() {
                    numberRecall.setText("Please pass the device to your supervisor...");
                    subRecall.setVisibility(View.VISIBLE);
                }
            }.start();

            subRecall.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    numberRecall.setVisibility(View.INVISIBLE);
                    numberRecall.setText(" ");
                    sec4count++;
                    questionNo++;
                    section4();
                }
            });
        } else if (sec4count>21&&sec4count<54){
            if (sec4count == 23) {
                question.setText(questionStr[questionNo]);
                new CountDownTimer(3000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        subRecall.setVisibility(View.INVISIBLE);
                        numberRecall.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        subRecall.setVisibility(View.VISIBLE);
                    }
                }.start();
                subRecall.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        subRecall.setVisibility(View.INVISIBLE);
                        sec4count++;
                        section4();
                    }
                });
            }
            else if (sec4count>23&&sec4count<53){
                new CountDownTimer(2000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        numberRecall.setVisibility(View.VISIBLE);
                        numberRecall.setText(letterList[sec4count-24]);
                        if(sec4count%2==0){
                            numberRecall.setTextColor(Color.MAGENTA);
                        }else{
                            numberRecall.setTextColor(Color.BLUE);
                        }
                    }

                    @Override
                    public void onFinish() {
                        numberRecall.setVisibility(View.INVISIBLE);
                        sec4count++;
                        section4();
                    }
                }.start();
            }
            else {
                numberRecall.setTextColor(Color.WHITE);
                Button btn_lettersYES = (Button) findViewById(R.id.button_mocaAgreeSV);
                Button btn_lettersNO = (Button) findViewById(R.id.button_mocaDisagreeSV);
                btn_lettersYES.setText("Yes");
                btn_lettersNO.setText("No");
                btn_lettersYES.setVisibility(View.VISIBLE);
                btn_lettersNO.setVisibility(View.VISIBLE);
                numberRecall.setText("Did the assessee make more than one mistake?");
                numberRecall.setVisibility(View.VISIBLE);
                btn_lettersYES.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sec4count++;
                        score++;
                        section4();
                    }
                });
                btn_lettersNO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sec4count++;
                        section4();
                    }
                });
                questionNo++;
                question.setText(questionStr[questionNo]);
            }
        }

        //Serial subtraction section
        else if (sec4count==54) {
            //preparing UI clearing non necessary buttons text etc
            questionNo++;
            question.setText(questionStr[questionNo]);
            Button btn_lettersYES = (Button) findViewById(R.id.button_mocaAgreeSV);
            Button btn_lettersNO = (Button) findViewById(R.id.button_mocaDisagreeSV);
            btn_lettersYES.setVisibility(View.INVISIBLE);
            btn_lettersNO.setVisibility(View.INVISIBLE);
            subRecall.setVisibility(View.VISIBLE);
            numberRecall.setVisibility(View.VISIBLE);
            numberRecall.setText("Ask the assessee to complete the task outlined above.");
            subRecall.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    subRecall.setVisibility(View.INVISIBLE);
                    sec4count++;
                    section4();
                }
            });
        } else if(sec4count==55){
            Button btn_subCorrect = (Button) findViewById(R.id.button_mocaAgreeSV);
            Button btn_subIncorrect = (Button) findViewById(R.id.button_mocaDisagreeSV);
            btn_subCorrect.setText("Assessee correct");
            btn_subIncorrect.setText("Assessee incorrect");
            btn_subCorrect.setVisibility(View.VISIBLE);
            btn_subIncorrect.setVisibility(View.VISIBLE);
            numberRecall.setText("1st answer:\n"+serialSub[0]);
            btn_subCorrect.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    sec4count++;
                    section4();
                }
            });
            btn_subIncorrect.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    mistakeCounter++;
                    sec4count++;
                    section4();
                }
            });

        } else if (sec4count>=56&&sec4count<60) {
            //Toast.makeText(getApplicationContext(), "TESTER tester 123", Toast.LENGTH_LONG).show();
            numberRecall.setText("Next answer:\n"+serialSub[sec4count-55]);
            Button btn_subCorrect = (Button) findViewById(R.id.button_mocaAgreeSV);
            Button btn_subIncorrect = (Button) findViewById(R.id.button_mocaDisagreeSV);
            subRecall.setOnClickListener(null);
            btn_subCorrect.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    sec4count++;
                    section4();
                }
            });
            btn_subIncorrect.setOnClickListener(new View.OnClickListener(){
                public void onClick(View v) {
                    mistakeCounter++;
                    sec4count++;
                    section4();
                }
            });

        } else {
            Button btn_subCorrect = (Button) findViewById(R.id.button_mocaAgreeSV);
            Button btn_subIncorrect = (Button) findViewById(R.id.button_mocaDisagreeSV);
            btn_subCorrect.setVisibility(View.INVISIBLE);
            btn_subIncorrect.setVisibility(View.INVISIBLE);
            numberRecall.setText(" ");
            if(mistakeCounter<=1){
                score=score+3;
                questionNo++;
                section5();
            }
            else if(mistakeCounter==2||mistakeCounter==3){
                score=score+2;
                questionNo++;
                section5();
            }
            else if(mistakeCounter==4){
                score++;
                questionNo++;
                section5();
            }
            else{
                questionNo++;
                section5();
            }
        }
    }
    public void section5(){
        //declaring elements for UI

        TextView question = (TextView) findViewById(R.id.textView_mocaQuestion);
        final TextView sentenceTV = (TextView)findViewById(R.id.textView_mocaRecall);
        final Button btn_langYes = (Button)findViewById(R.id.button_mocaLangYES);
        final Button btn_langNo = (Button)findViewById(R.id.button_mocaLangNO);
        final Button btn_start = (Button)findViewById(R.id.button_submitRecall);
        final EditText edtTxt_fWords = (EditText)findViewById(R.id.editText_mocaRecall);

        //first sentence
        if (section5count==0){
            sentenceTV.setText("I only know that John is the one to help today.");
            question.setText(questionStr[questionNo]);
            new CountDownTimer(2500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btn_langYes.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v) {
                            btn_langYes.setVisibility(View.INVISIBLE);
                            btn_langNo.setVisibility(View.INVISIBLE);
                            score++;
                            section5count++;
                            section5();
                        }
                    });
                    btn_langNo.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v) {
                            btn_langYes.setVisibility(View.INVISIBLE);
                            btn_langNo.setVisibility(View.INVISIBLE);
                            section5count++;
                            section5();
                        }
                    });

                }

                @Override
                public void onFinish() {
                    btn_langYes.setVisibility(View.VISIBLE);
                    btn_langNo.setVisibility(View.VISIBLE);
                }
            }.start();
        } else if (section5count==1){
            sentenceTV.setText("The cat always hid under the couch when the dogs were in the room.");
            new CountDownTimer(2500, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    btn_langYes.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v) {
                            sentenceTV.setText("");
                            score++;
                            section5count++;
                            questionNo++;
                            section5();
                        }
                    });
                    btn_langNo.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v) {
                            sentenceTV.setText("");
                            section5count++;
                            questionNo++;
                            section5();
                        }
                    });

                }

                @Override
                public void onFinish() {
                    btn_langYes.setVisibility(View.VISIBLE);
                    btn_langNo.setVisibility(View.VISIBLE);
                }
            }.start();
        } else{//Maximum number of words beginning with 'F' in one minute
            btn_langYes.setOnClickListener(null);
            btn_langNo.setOnClickListener(null);
            btn_langYes.setVisibility(View.INVISIBLE);
            btn_langNo.setVisibility(View.INVISIBLE);
            question.setText(questionStr[questionNo]);

            if (section5count==2){
                //start button
                btn_start.setText("Start");
                btn_start.setBackgroundColor(Color.GREEN);
                btn_start.setTextColor(Color.WHITE);
                btn_start.setVisibility(View.VISIBLE);
                btn_start.setOnClickListener(new View.OnClickListener(){
                    public void onClick(View v) {
                        btn_start.setVisibility(View.INVISIBLE);
                        section5count++;
                        section5();
                    }
                });

            } else {
                if (timerSec5>0){
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            sentenceTV.setText(Integer.toString(timerSec5));
                            sentenceTV.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onFinish() {
                            timerSec5--;
                            section5();
                        }
                    }.start();
                } else if (section5count==3){
                    sentenceTV.setText("0");
                    new CountDownTimer(1000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            btn_start.setText("Okay");
                            btn_start.setTextColor(Color.WHITE);
                            btn_start.setBackgroundColor(Color.GRAY);
                            btn_start.setOnClickListener(new View.OnClickListener(){
                                public void onClick(View v) {
                                    section5count++;
                                    section5();
                                }
                            });
                        }

                        @Override
                        public void onFinish() {
                            sentenceTV.setTextColor(Color.RED);
                            sentenceTV.setText("Time up!");
                            btn_start.setVisibility(View.VISIBLE);
                            //
                        }
                    }.start();
                } else if (section5count==4){
                    sentenceTV.setTextColor(Color.WHITE);
                    sentenceTV.setText("How many words did the assessee name?");
                    edtTxt_fWords.setText("");
                    edtTxt_fWords.setVisibility(View.VISIBLE);
                    edtTxt_fWords.setInputType(InputType.TYPE_CLASS_NUMBER);
                    btn_start.setText("Submit");
                    btn_start.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v) {
                            if(((EditText)findViewById(R.id.editText_mocaRecall))==null){
                                edtTxt_fWords.setError("Cannot be blank!");
                            }else{
                                int input  = Integer.parseInt(((EditText)findViewById(R.id.editText_mocaRecall)).getText().toString());
                                btn_start.setText("Okay");
                                edtTxt_fWords.setVisibility(View.INVISIBLE);
                                if (input>=11){
                                    score++;
                                    section5count++;
                                    section5();
                                }
                                else{
                                    section5count++;
                                    section5();
                                }
                            }


                        }
                    });
                } else{
                    sentenceTV.setText("Please hand the phone back to the assessee.");
                    btn_start.setOnClickListener(new View.OnClickListener(){
                        public void onClick(View v) {
                            sentenceTV.setText("");
                            btn_start.setVisibility(View.INVISIBLE);
                            edtTxt_fWords.setInputType(InputType.TYPE_CLASS_TEXT);
                            questionNo++;
                            section6();
                        }
                    });
                }
            }
        }
    }
    public void section6(){
        final TextView objects = (TextView)findViewById(R.id.textView_mocaRecall);
        TextView question = (TextView)findViewById(R.id.textView_mocaQuestion);
        final EditText input = (EditText)findViewById(R.id.editText_mocaRecall);
        final Button btn_submit = (Button)findViewById(R.id.button_submitRecall);
        question.setText(questionStr[questionNo]);
        if (section6count==0){
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    objects.setText("Train   |   Bicycle");
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String input  = ((EditText)findViewById(R.id.editText_mocaRecall)).getText().toString().toLowerCase();
                            if(input.contains("transport")||input.contains("wheels")){
                                score++;
                                section6count++;
                                section6();
                            }else{
                                section6count++;
                                section6();
                            }
                        }
                    });
                }
                @Override
                public void onFinish() {
                    objects.setVisibility(View.VISIBLE);
                    input.setText("");
                    input.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                }
            }.start();
        }else{
            input.setVisibility(View.INVISIBLE);
            input.setText("");
            btn_submit.setVisibility(View.INVISIBLE);
            new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    objects.setText("Watch   |   Ruler");
                    btn_submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String input  = ((EditText)findViewById(R.id.editText_mocaRecall)).getText().toString().toLowerCase();
                            if(input.contains("measur")){
                                score++;
                                section6count++;
                                questionNo++;
                                objects.setVisibility(View.INVISIBLE);
                                section7();
                            }else{
                                section6count++;
                                questionNo++;
                                objects.setVisibility(View.INVISIBLE);
                                section7();
                            }
                        }
                    });
                }
                @Override
                public void onFinish() {
                    objects.setVisibility(View.VISIBLE);
                    input.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                }
            }.start();
        }

    }

    public void section7(){
        EditText input = (EditText)findViewById(R.id.editText_mocaRecall);
        Button btn_submit = (Button)findViewById(R.id.button_submitRecall);
        TextView txtView_deRe = (TextView)findViewById(R.id.textView_mocaRecall);
        TextView question = (TextView)findViewById(R.id.textView_mocaQuestion);
        question.setText(questionStr[questionNo]);
        input.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.VISIBLE);
        if (section7count==0){
            input.setText("");
            recalledList = "";
            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputStr = ((EditText)findViewById(R.id.editText_mocaRecall)).getText().toString().toLowerCase();
                    section7count++;
                    if (recalledList.contains(inputStr)){
                        recalledList = " " + recalledList;
                        section7();
                    }else{
                        recalledList = inputStr + " " + recalledList;
                        section7();
                    }

                }
            });
        } else if (section7count>0&&section7count<5){
            input.setText("");
        }else{

            if (recalledList.contains("face")){
                score++;
            }
            if (recalledList.contains("velvet")){
                score++;
            }
            if (recalledList.contains("church")){
                score++;
            }
            if (recalledList.contains("daisy")){
                score++;
            }
            if (recalledList.contains("red")){
                score++;
            }

            questionNo++;
            input.setVisibility(View.INVISIBLE);
            btn_submit.setVisibility(View.INVISIBLE);
            section8();
        }
    }
    public void section8(){
        TextView question = (TextView)findViewById(R.id.textView_mocaQuestion);
        EditText input = (EditText)findViewById(R.id.editText_mocaRecall);
        Button btn_okay = (Button)findViewById(R.id.button_submitRecall);
        String[] subQuestion = {
                "What day is it?",
                "What month is it?",
                "What year is it?",
                "",
                "Which country are you in?"
        };
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        String countryName;
        String cityName;

        /*/get permissions and/or location info
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location!=null){
                                    lat = location.getLatitude();
                                    longt = location.getLatitude();
                                }
                                else{
                                    lat=53.345003;
                                    longt=-6.267501;
                                }
                            }
                        });
            }else{
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }
        }
        else{
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    lat = location.getLatitude();
                    longt = location.getLatitude();
                }
            });
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(lat, longt, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //puts location data in strings for comparison
        if(addresses!=null){
            countryName = addresses.get(0).getCountryName();
        }else{
            countryName="Error";
            cityName="Error";
        }
        */
        if(section8count==0){
            question.setText(subQuestion[section8count]);
            btn_okay.setVisibility(View.VISIBLE);
            input.setVisibility(View.VISIBLE);
            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String userInput = ((EditText)findViewById(R.id.editText_mocaRecall)).getText().toString();
                    recalledList = recalledList + userInput;
                    section8count++;
                    section8();
                }
            });
        }else if(section8count==1) {
            question.setText(subQuestion[section8count]);

            String[] days = {"sun", "mon", "tues", "wed", "thurs", "fri", "sat"};
            int currentDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
            if (recalledList.contains(days[currentDay-1])){
                score++;
            }
            input.setText("");
        }else if (section8count==2){
            question.setText(subQuestion[section8count]);
            String[] months = {"jan", "feb", "mar", "apr", "may", "jun", "jul", "aug", "sep", "oct", "nov", "dec",};
            int currentMonth = Calendar.getInstance().get(Calendar.MONTH);
            if (recalledList.contains(months[currentMonth])){
                score++;
            }
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setText("");
        }else if(section8count==3){
            question.setText(subQuestion[section8count]);
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            if (recalledList.contains(String.valueOf(currentYear))){
                score++;
            }
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setText("");
            finishMOCA();
        }
    }
}
