package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SettingsActivity extends AppCompatActivity {

    private TextView signout;
    private TextView username;
    private TextView messages;
    private TextView personalSettings;
    public static Activity activity;
    private SharedPreferences sharedPreferences;
    private final String LOGIN_FILE = "login";
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);
        activity = this;
        setIds();
        username.setText(ApplicationModel.getCurrentDriver().getName());
        setClickListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
    }

    private void setClickListeners() {

        messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToAllChatsActivity();
            }
        });

        personalSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPersonalSettingsActivity();
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                logout();
                MainActivity.activity.finish();
                goToLoginActivity();
            }
        });

    }

    private void goToPersonalSettingsActivity() {
        Intent intent = new Intent(this,PersonalSettingsActivity.class);
        startActivity(intent);
    }

    private void goToAllChatsActivity() {

        Intent intent = new Intent(this,AllChatsActivity.class);
        startActivity(intent);
        finish();
    }

    private void logout() {

        sharedPreferences.edit().putBoolean("logged",false).apply();
        ApplicationModel.setCurrentDriver(null);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    public void goToLoginActivity(){

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();

    }

    public void setIds(){
        signout = (TextView)findViewById(R.id.logout);
        messages = (TextView)findViewById(R.id.messages);
        username = (TextView)findViewById(R.id.username);
        personalSettings = (TextView)findViewById(R.id.personal);
    }
}
