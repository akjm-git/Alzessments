package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.regex.Pattern;

public class NewUser extends AppCompatActivity {
    LoginDataBaseAdapter loginDataBaseAdapter;
    private static final Pattern USERNAME_PATTERN =
            Pattern.compile("^[A-Za-z0-9_-]*$"); // no special char
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("(?=^.{5,9}$)((?!.*\\s)(?=.*[A-Z])(?=.*[a-z]))(?=(1)(?=.*\\d)|.*[^A-Za-z0-9])^.*");
    private static final Pattern DOB_PATTERN =
            Pattern.compile("^([0-2][0-9]|(3)[0-1])(\\/)(((0)[0-9])|((1)[0-2]))(\\/)\\d{4}$");
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Z]+$");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        //get instance of db
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
    }

    public void SignUpOK(View view){
        //for error on type fields
        EditText username_input = (EditText)findViewById(R.id.editText_nUsername);
        EditText password_input = (EditText)findViewById(R.id.editText_nPassword);
        EditText CPassword_input = (EditText)findViewById(R.id.editText_NCPassword);
        EditText email_input = (EditText)findViewById(R.id.editText_nEmail);
        EditText dob_input = (EditText)findViewById(R.id.editText_nDOB);

        String username = ((EditText)findViewById(R.id.editText_nUsername)).getText().toString();
        String password = ((EditText)findViewById(R.id.editText_nPassword)).getText().toString();
        String CPassword = ((EditText)findViewById(R.id.editText_NCPassword)).getText().toString();
        String userEmail = ((EditText)findViewById(R.id.editText_nEmail)).getText().toString();
        String userDOB = ((EditText)findViewById(R.id.editText_nDOB)).getText().toString();

        //Get userGender
        String userGender;
        RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        if(radioGroup.getCheckedRadioButtonId() == R.id.radioButton_female) {
            userGender = "female";
        }
        else{
            userGender = "male";
        }


        //Validation
        int usernameVal = 0;
        int passwordVal = 0;
        int emailVal = 0;
        int DOBVal = 0;

        //username
        if (username_input.getText().toString().isEmpty()){
            username_input.setError("Username cannot be blank!");
        }
        else{
            if (USERNAME_PATTERN.matcher(username).matches()){
                if(loginDataBaseAdapter.usernameSearch(username)==false){
                    username_input.setError("Username already taken!");
                }
                else{
                    usernameVal = 1;
                }
            }
            else{
                username_input.setError("Must not contain any special characters");
            }
        }

        //password
        if (password_input.getText().toString().isEmpty()){
            password_input.setError("Password cannot be blank!");
        }
        else{
            if (PASSWORD_PATTERN.matcher(password).matches()){
                passwordVal = 1;
            }
            else{
                password_input.setError("Must be 5-9 characters and contain a capital letter," +
                        " lowercase letter, number and special character");
            }
        }

        //email
        if (email_input.getText().toString().isEmpty()){
            email_input.setError("Email cannot be blank!");
        }
        else{
            if (Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()){
                emailVal = 1;
            }
            else{
                email_input.setError("Enter valid email");
            }
        }

        //age
        if(dob_input.getText().toString().isEmpty()){
            dob_input.setError("DOB cannot be blank!");
        }
        else{
            if(DOB_PATTERN.matcher(userDOB).matches()){
                DOBVal = 1;
            }
            else{
                dob_input.setError("Must be in DD/MM/YYYY");
            }
        }

        //Signup
        if (password.equals(CPassword)){
            if ((usernameVal==1)&&(passwordVal==1)&&(emailVal==1)&&(DOBVal==1)){
                loginDataBaseAdapter.insertEntry(username, password, userGender, userEmail, userDOB);
                Toast.makeText(getApplicationContext(),
                        "Account created! Sign in...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent (NewUser.this, MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getApplicationContext(),
                        "Error creating account. Please make sure you have filled all fields correctly", Toast.LENGTH_LONG).show();
            }
        }
        else{
            CPassword_input.setError("Passwords do not match!");
        }
    }
}