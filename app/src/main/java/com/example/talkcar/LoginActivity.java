package com.example.talkcar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talkcar.Helpers.FieldsChecker;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private Button signInBtn;
    private Button signUpBtn;
    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private FirebaseAuth mFirebaseAuth;
    private FieldsChecker checker;
    private int error;
    public static Activity activity;
    private final String LOGIN_FILE = "login";
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SharedPreferences sharedPreferences;
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);
        setIds();
        activity = this;

        if(sharedPreferences.getBoolean("logged",false)){
            goToWaitingActivity();
        }

        mFirebaseAuth = FirebaseAuth.getInstance();
        checker = new FieldsChecker();

        setClickListeners();
    }

    @Override
    protected void onResume() {

        super.onResume();
        error = getIntent().getIntExtra("fail",0);
        if(error == 1){
            Toast.makeText(LoginActivity.this, "email or password were incorrect.", Toast.LENGTH_SHORT).show();
        }
        error = 0;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setClickListeners(){

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFireBaseUser();
            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });

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

    private void loginFireBaseUser(){

        if(!checker.checkLoginFields(emailPlaceHolder, passwordPlaceHolder))
            return;

            goToWaitingActivity();

    }

    private void setIds() {

        signInBtn = (Button)findViewById(R.id.signin_btn);
        signUpBtn = (Button) findViewById(R.id.signup_btn);
        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
    }


    private void goToWaitingActivity(){

        Intent intent = new Intent(this,WaitingActivity.class);
        intent.putExtra("operation",0);
        intent.putExtra("email",emailPlaceHolder.getText().toString());
        intent.putExtra("password",passwordPlaceHolder.getText().toString());
        intent.putExtra("autologin",true);

        startActivity(intent);
        finish();

    }

    private void goToSignupActivity(){

        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);

    }

}
