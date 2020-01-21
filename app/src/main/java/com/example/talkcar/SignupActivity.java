package com.example.talkcar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements OnInputListener {

    private EditText firstNamePlaceHolder;
    private EditText lastNamePlaceHolder;
    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private EditText passwordConfirmationPlaceHolder;
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

        AddNewCarDialog dialog = new AddNewCarDialog();
        dialog.show(getSupportFragmentManager(),"AddNewCarDialog");
    }

    private void createFireBaseUser(){

        if(NewCarForm.allForms.size() == 0) {
            Toast.makeText(this, "You need to have at least one car", Toast.LENGTH_SHORT).show();
            return;
        }

        if(!checker.checkUserDetailsFields(firstNamePlaceHolder,lastNamePlaceHolder,emailPlaceHolder, passwordPlaceHolder,passwordConfirmationPlaceHolder))
            return;

            for(int i = 0; i < NewCarForm.allForms.size(); i++){
                if(!checker.checkCarDetailsFields(NewCarForm.allForms.get(i).getCarNumberPlaceHolder(),NewCarForm.allForms.get(i).getNicknamePlaceHolder())){
                    return;
                }
            }

        goToWaitingActivity();

    }

    private void setIds() {

        allFormContainer = (LinearLayout)findViewById(R.id.all_forms_container);
        firstNamePlaceHolder = (EditText)findViewById(R.id.firstname_placeholder);
        lastNamePlaceHolder = (EditText)findViewById(R.id.lastname_placeholder);
        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        passwordConfirmationPlaceHolder = (EditText)findViewById(R.id.password_placeholder_confirmation);
        signInBtn = (Button)findViewById(R.id.signin_btn);
        addCar = (ImageView)findViewById(R.id.plus_sign);
    }

    @Override
    public void sendInput(Car car) {

       TextView carNumber = dynamicallyXML.createTextView(this,car.getCarNumber(),10,Color.BLACK,Gravity.CENTER,0,0,0,0);
       TextView nickname = dynamicallyXML.createTextView(this,car.getNickname(),10,Color.BLACK,Gravity.CENTER,0,0,0,0);

       //card id here is 0 because it not neccesary at this point, so we give a fectiv index 0;
       CarView card = new CarView(carNumber,nickname,car.getEmojiId(),0,allFormContainer,this);
    }

    @Override
    public void sendInput(int index) {

    }

    private void goToWaitingActivity(){

        Intent intent = new Intent(this,WaitingActivity.class);
        intent.putExtra("operation",1);
        intent.putExtra("email",emailPlaceHolder.getText().toString());
        intent.putExtra("password",passwordPlaceHolder.getText().toString());
        intent.putExtra("firstname",firstNamePlaceHolder.getText().toString());
        intent.putExtra("lastname",lastNamePlaceHolder.getText().toString());
        startActivity(intent);
        finish();
    }
}
