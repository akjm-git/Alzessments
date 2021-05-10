package com.example.alzessmentslocal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LoginDataBaseAdapter loginDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create instance of database
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
    }
    public void signIn(View view){
        try{
            String username = ((EditText)findViewById(R.id.editText_username)).getText().toString();
            String password = ((EditText)findViewById(R.id.editText_password)).getText().toString();

            //get pw stored for username
            String storedPassword = loginDataBaseAdapter.getSingleEntry(username);

            if (password.equals(storedPassword)) {
                //storing the user login name in preferences for custom welcome and profile page
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("username", username);
                editor.commit();

                //display successful login and switch to application home screen
                Toast.makeText(MainActivity.this, "Successful Login",
                        Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, Home.class);
                intent.putExtra("Name", username);
                startActivity(intent);
            }
            else {
                Toast.makeText(MainActivity.this,
                        "No records! Please sign up...", Toast.LENGTH_LONG).show();
            }
        }
        catch (Exception ex){
            Log.e("Error", "error login");
        }
    }

    public void newUser(View view){
        Intent intent = new Intent(MainActivity.this, NewUser.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        //close the db
        loginDataBaseAdapter.close();
    }
}