package com.example.talkcar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

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
        createAddCarForm();

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

                createAddCarForm();
            }
        });
    }

    private void createAddCarForm() {

        NewCarForm newCarForm = new NewCarForm(this,allFormContainer);
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
}
