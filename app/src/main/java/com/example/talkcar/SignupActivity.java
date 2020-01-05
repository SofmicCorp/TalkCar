package com.example.talkcar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private EditText carNumberPlaceHolder;
    private Button signInBtn;
    private Database database;
    private boolean emojiOneWasPicked;
    private boolean emojiTwoWasPicked;
    private boolean emojiThreeWasPicked;
    private ImageView emojiOne;
    private ImageView emojiTwo;
    private ImageView emojiThree;

    FirebaseAuth mFirebaseAuth;
    Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setIds();
        mFirebaseAuth = FirebaseAuth.getInstance();
        driver  = new Driver();
        database = new Database();
        setClickListeners();

    }

    private void setClickListeners(){

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFireBaseUser();
            }
        });

        emojiOne.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                emojiOneWasPicked = true;
                emojiTwoWasPicked = false;
                emojiThreeWasPicked = false;
                showPickedEmoji();
            }
        });

        emojiTwo.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                emojiOneWasPicked = false;
                emojiTwoWasPicked = true;
                emojiThreeWasPicked = false;
                showPickedEmoji();
            }
        });

        emojiThree.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                emojiOneWasPicked = false;
                emojiTwoWasPicked = false;
                emojiThreeWasPicked = true;
                showPickedEmoji();
            }
        });


    }

    private void showPickedEmoji() {

        if(emojiOneWasPicked){

            fillCircle(emojiOne);


        } else{
            emojiOne.setBackgroundColor(Color.HSVToColor(new float[]{0, 0, 100}));
        }

        if(emojiTwoWasPicked){

            fillCircle(emojiTwo);

        }else{

            emojiTwo.setBackgroundColor(Color.HSVToColor(new float[]{0, 0, 100}));


        }
        if(emojiThreeWasPicked){

            fillCircle(emojiThree);

        }else {
            emojiThree.setBackgroundColor(Color.HSVToColor(new float[]{0, 0, 100}));

        }
    }

    private void fillCircle(ImageView emoji) {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(100);
        gd.setStroke(4, Color.rgb(44,167,239));
        emoji.setBackgroundDrawable(gd);
    }


    private void createFireBaseUser(){
        final String email = emailPlaceHolder.getText().toString().trim();
        String pwd = passwordPlaceHolder.getText().toString().trim();
        final String carNumber = carNumberPlaceHolder.getText().toString();

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
                        createDriver(email,carNumber);
                        database.saveDriver(driver);

                        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }else{
            Toast.makeText(SignupActivity.this,"Error Occurred!", Toast.LENGTH_SHORT);
        }
    }

    private void createDriver(String email, String carNumber) {
        driver.setEmail(email);
        driver.setCarNumber(carNumber);
    }


    private void setIds() {

        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        carNumberPlaceHolder = (EditText)findViewById(R.id.car_number_placeholder);
        signInBtn = (Button)findViewById(R.id.signin_btn);
        emojiOne = (ImageView)findViewById(R.id.driver1);
        emojiTwo = (ImageView)findViewById(R.id.driver2);
        emojiThree = (ImageView)findViewById(R.id.driver3);
    }

}
