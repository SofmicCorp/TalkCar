package com.example.talkcar;

import android.content.Intent;
import android.os.Bundle;
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

public class SignupActivity extends AppCompatActivity {

    EditText emailPlaceHolder;
    EditText passwordPlaceHolder;
    EditText carNumberPlaceHolder;
    Button signInBtn;

    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setIds();
        mFirebaseAuth = FirebaseAuth.getInstance();
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFireBaseUser();
            }
        });
    }


    private void createFireBaseUser(){
        String email = emailPlaceHolder.getText().toString();
        String pwd = passwordPlaceHolder.getText().toString();
        if(email.isEmpty()){
            emailPlaceHolder.setError("Please enter email");
            emailPlaceHolder.requestFocus();
        }else if(pwd.isEmpty()){
            passwordPlaceHolder.setError("Please enter your password");
        }else if(email.isEmpty() && pwd.isEmpty()){
            Toast.makeText(SignupActivity.this,"Fields Are Empty!", Toast.LENGTH_SHORT);
        }else if(!(email.isEmpty() && pwd.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(SignupActivity.this,"Sign in unsuccessful, please try again!", Toast.LENGTH_SHORT);
                    }else{
                        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }else{
            Toast.makeText(SignupActivity.this,"Error Occurred!", Toast.LENGTH_SHORT);
        }
    }


    private void setIds() {

        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        carNumberPlaceHolder = (EditText)findViewById(R.id.car_number_placeholder);
        signInBtn = (Button)findViewById(R.id.signin_btn);
    }

}
