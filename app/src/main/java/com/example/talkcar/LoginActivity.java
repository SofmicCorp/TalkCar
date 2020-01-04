package com.example.talkcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;

public class LoginActivity extends AppCompatActivity {


    Button signInBtn;
    EditText emailPlaceHolder;
    EditText passwordPlaceHolder;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setIds();
        setClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void setClickListeners(){

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMainActivity();
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSignupActivity();
            }
        });

    }

    private void setIds() {

        signInBtn = (Button)findViewById(R.id.signin_btn);
        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        signupText = (TextView)findViewById(R.id.signup_text);
    }

    private void goToMainActivity(){

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void goToSignupActivity(){

        Intent intent = new Intent(this,SignupActivity.class);
        startActivity(intent);

    }
}
