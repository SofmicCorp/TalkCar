package com.example.talkcar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements OnInputListener {

    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private Button signInBtn;
    private ImageView addCar;
    private Database databaseRef;
    private FirebaseAuth mFirebaseAuth;
    private Driver driver;
    private LinearLayout allFormContainer;
    private DynamicallyXML dynamicallyXML;
    private FieldsChecker checker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setIds();
        NewCarForm.removeAllForms();
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseRef = new Database(new MD5());
        dynamicallyXML = new DynamicallyXML();
        checker = new FieldsChecker();
        setClickListeners();


    }

    private void setClickListeners(){

        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createFireBaseUser();
            }
        });

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openNewCarDialog();
            }
        });
    }



    private void openNewCarDialog() {

        AddNewCarCarDialog dialog = new AddNewCarCarDialog();
        dialog.show(getSupportFragmentManager(),"AddNewCarDialog");
    }

    private void createFireBaseUser(){

        if(!checker.checkUserDetailsFields(emailPlaceHolder, passwordPlaceHolder))
            return;

            for(int i = 0; i < NewCarForm.allForms.size(); i++){
                if(!checker.checkCarDetailsFields(NewCarForm.allForms.get(i).getCarNumberPlaceHolder(),NewCarForm.allForms.get(i).getNicknamePlaceHolder())){
                    return;
                }
            }
            mFirebaseAuth.createUserWithEmailAndPassword(emailPlaceHolder.getText().toString(), passwordPlaceHolder.getText().toString()).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(SignupActivity.this,"Sign in unsuccessful, please try again!", Toast.LENGTH_SHORT);
                    }else{
                       saveDriverToDatabase(emailPlaceHolder.getText().toString());
                       Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                       startActivity(intent);
                       finish();
                    }
                }
            });
    }


    private void saveDriverToDatabase(String email){

        driver = new Driver(email);
        for(int i = 0; i < NewCarForm.allForms.size(); i++){
            String carNumber = NewCarForm.allForms.get(i).getCarNumberPlaceHolder().getText().toString();
            String nickName = NewCarForm.allForms.get(i).getNicknamePlaceHolder().getText().toString();
            String emojiId = NewCarForm.allForms.get(i).getEmojiID();
            driver.addCar(new Car(carNumber, nickName,emojiId));
        }
        databaseRef.saveDriver(driver);

        //Set current driver on app
        LoginActivity.applicationModel.setCurrentDriver(driver);
    }

    private void setIds() {

        allFormContainer = (LinearLayout)findViewById(R.id.all_forms_container);
        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        signInBtn = (Button)findViewById(R.id.signin_btn);
        addCar = (ImageView)findViewById(R.id.plus_sign);
    }

    @Override
    public void sendInput(Car car) {

        ImageView emoji;
        final LinearLayout carContainer = new LinearLayout(this);
        TextView addedCar = dynamicallyXML.createTextView(this,car.getCarNumber(),20, Color.BLACK, Gravity.CENTER,20,50,10,10);
        if(car.getEmojiId().equals("1")) {
             emoji = dynamicallyXML.createImageView(this, R.drawable.driver1, 150, 100, Gravity.CENTER, 230, 5, 5, 5);
        } else if(car.getEmojiId().equals("2")){
            emoji = dynamicallyXML.createImageView(this, R.drawable.driver2, 150, 100, Gravity.CENTER, 230, 5, 5, 5);
        } else {
            emoji = dynamicallyXML.createImageView(this, R.drawable.driver3, 150, 100, Gravity.CENTER, 230, 5, 5, 5);
        }

        ImageView delete = dynamicallyXML.createImageView(this,R.drawable.minussign,70,70,Gravity.CENTER,-600,5,0,5);


        carContainer.addView(emoji);
        carContainer.addView(addedCar);
        carContainer.addView(delete);

        allFormContainer.addView(carContainer);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allFormContainer.removeView(carContainer);
            }
        });

    }
}
