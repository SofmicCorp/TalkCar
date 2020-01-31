package com.example.talkcar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;

public class LoginActivity extends AppCompatActivity {

    private Button signInBtn;
    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private TextView signupText;
    private FirebaseAuth mFirebaseAuth;
    private FieldsChecker checker;
    public  Database databaseRef;
    private int error;
    public static Activity activity;
    private final String LOGIN_FILE = "login";
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private SharedPreferences sharedPreferences;

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

        databaseRef = new Database(new MD5());
        mFirebaseAuth = FirebaseAuth.getInstance();
        checker = new FieldsChecker();

        setClickListeners();

        //Creating fake users script
//        for(int i = 0; i < 5000; i++){
//            Driver driver = new Driver("nana","nana" + i  + "@gmail.com") ;
//            driver.addCar(new Car("" + i,"babushka","" + 1));
//            Log.d("BUBA", "driver : " + driver.getEmail());
//            databaseRef.saveDriver(driver);
//        }

//        databaseRef.searchCarByCarNumber("4803", new OnGetDataListener() {
//            @Override
//            public void onSuccess(Driver driver) {
//
//                Log.d("buba", "onSuccess:  founded! driver  is "+ driver.getName() + " and car number is " + ApplicationModel.getLastCarNumberSearch().getCarNumber());
//
//            }
//
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onFailure() {
//
//            }
//        });
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

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });

    }

    private void loginFireBaseUser(){

        if(!checker.checkLoginFields(emailPlaceHolder, passwordPlaceHolder))
            return;

            goToWaitingActivity();

    }

    private void setIds() {

        signInBtn = (Button)findViewById(R.id.signin_btn);
        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        signupText = (TextView)findViewById(R.id.signup_text);
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
