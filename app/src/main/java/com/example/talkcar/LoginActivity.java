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

    Button signInBtn;
    EditText emailPlaceHolder;
    EditText passwordPlaceHolder;
    TextView signupText;
    FirebaseAuth mFirebaseAuth;
    FieldsChecker checker;
    public  Database databaseRef;
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
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null) {
                    Toast.makeText(LoginActivity.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                }else{
                    Toast.makeText(LoginActivity.this, "Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };
        setClickListeners();

    }

    @Override
    protected void onStart() {
        super.onStart();
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

        if(!checker.checkUserDetailsFields(emailPlaceHolder, passwordPlaceHolder))
            return;

            mFirebaseAuth.signInWithEmailAndPassword(emailPlaceHolder.getText().toString(), passwordPlaceHolder.getText().toString()). addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this,"Login Error, Please Try Again", Toast.LENGTH_SHORT).show();
                    }else {
                        //finding the current driver in database
                        databaseRef.updateCurrentDriverByEmail(emailPlaceHolder.getText().toString(), new OnGetDataListener(){

                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                goToMainActivity();
                            }

                            @Override
                            public void onStart() {
                                Log.d("CHECK", "Wait for data... ");
                            }

                            @Override
                            public void onFailure() {

                                Log.d("CHECK", "Data retrieving has been failed. ");
                            }
                        });
                    }
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
