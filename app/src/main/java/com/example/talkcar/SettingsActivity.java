package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {

    private TextView signout;
    private TextView username;
    private SharedPreferences sharedPreferences;
    private final String LOGIN_FILE = "login";
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);
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
        username = (TextView)findViewById(R.id.username);
    }
}
