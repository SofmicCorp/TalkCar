package com.example.talkcar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private int fail;
    public static ApplicationModel applicationModel;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setIds();
        databaseRef = new Database(new MD5());
        applicationModel = new ApplicationModel();
        mFirebaseAuth = FirebaseAuth.getInstance();
        checker = new FieldsChecker();


        //Check with mor what is that code????
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
//                if(mFirebaseUser != null) {
//                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
//                    goToMainActivity();
//                }else{
//                    Toast.makeText(LoginActivity.this, "Please Login",Toast.LENGTH_SHORT).show();
//                }
//            }
//        };

        setClickListeners();

    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fail = getIntent().getIntExtra("fail",0);
        if(fail == 1){
            Toast.makeText(LoginActivity.this, "email or password were incorrect.", Toast.LENGTH_SHORT).show();
        }
        fail = 0;
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

        startActivity(intent);
    }

    private void goToSignupActivity(){

        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);

    }

}
