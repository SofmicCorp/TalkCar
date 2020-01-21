package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private TextView signout;
    private SharedPreferences sharedPreferences;
    private final String LOGIN_FILE = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);
        setIds();
        setClickListeners();
    }

    private void setClickListeners() {

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
                MainActivity.activity.finish();
                goToLoginActivity();
            }
        });
    }

    private void logout() {

        sharedPreferences.edit().putBoolean("logged",false).apply();
        LoginActivity.applicationModel.setCurrentDriver(null);
    }

    public void goToLoginActivity(){

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void setIds(){
        signout = (TextView)findViewById(R.id.signout);
    }
}
