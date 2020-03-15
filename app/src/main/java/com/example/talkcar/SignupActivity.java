package com.example.talkcar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.talkcar.Cars.AddNewCarDialog;
import com.example.talkcar.Cars.Car;
import com.example.talkcar.Cars.CarForm;
import com.example.talkcar.Cars.LicencePlateView;
import com.example.talkcar.Driver.Driver;
import com.example.talkcar.Helpers.DynamicallyXML;
import com.example.talkcar.Helpers.FieldsChecker;
import com.example.talkcar.Interfaces.OnInputListener;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity implements OnInputListener {

    private EditText namePlaceHolder;
    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private Button signInBtn;
    private ImageView addCar;
    private FirebaseAuth mFirebaseAuth;
    private LinearLayout allFormContainer;
    private DynamicallyXML dynamicallyXML;
    private FieldsChecker checker;
    public static Activity activity;
    private int ERROR_WEAK_PASSWORD = 1;
    private int ERROR_MALFORMED_EMAIL = 2;
    private int ERROR_EXISTS_EMAIL = 3;
    private int ERROR_UNKNOWN = 4;
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setIds();
        CarForm.removeAllForms();
        LicencePlateView.removeAllCarViews();
        mFirebaseAuth = FirebaseAuth.getInstance();
        dynamicallyXML = new DynamicallyXML();
        checker = new FieldsChecker();
        activity = this;
        setClickListeners();
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        int error = intent.getIntExtra("error", 0);

          if(error ==  ERROR_MALFORMED_EMAIL){
            emailPlaceHolder.setError("Email contains illegal values.");
        } else if(error == ERROR_EXISTS_EMAIL){
            emailPlaceHolder.setError("Email is already in use.");
        }else if(error == ERROR_WEAK_PASSWORD){
            passwordPlaceHolder.setError("Password should include at lease 6 characters.");
        } else if(error == ERROR_UNKNOWN){
            Toast.makeText(this, "Something went wrong...", Toast.LENGTH_SHORT).show();
        }
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

        if(!checker.checkUserDetailsFields(namePlaceHolder,emailPlaceHolder, passwordPlaceHolder)) {
            return;
        }

        if(CarForm.allForms.size() == 0) {
            Toast.makeText(this, "You need to have at least one car", Toast.LENGTH_SHORT).show();
            return;
        }
        goToWaitingActivity();

    }

    private void setIds() {

        allFormContainer = (LinearLayout)findViewById(R.id.all_forms_container);
        namePlaceHolder = (EditText)findViewById(R.id.name_holder);
        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        signInBtn = (Button)findViewById(R.id.signin_btn);
        addCar = (ImageView)findViewById(R.id.plus_sign);
    }

    @Override
    public void sendInput(Car car) {

        //get the new car details and create a card view to that car
       TextView nickname = dynamicallyXML.createTextView(this,car.getNickname(),"sans-serif-smallcaps",40, Color.BLACK, Gravity.CENTER,20,50,10,10);
       LicencePlateView licencePlateView = new LicencePlateView(nickname, CarForm.allForms.size() - 1 ,allFormContainer,this,this,car.getCarNumber());
       LicencePlateView.allLicencePlateViews.add(licencePlateView);
    }

    @Override
    public void sendInputToEdit(Car car, LicencePlateView licencePlateView, CarForm carForm) {

        StringBuilder stringBuilder;
        updateFormValues(carForm,car);
        if(car.getNickname().equals(car.getCarNumber())) {
            if(car.getNickname().length() == 7) {
                stringBuilder = FieldsChecker.addDashesSevenDigit(car.getCarNumber());
            } else {
                stringBuilder = FieldsChecker.addDashesEightDigit(car.getCarNumber());
            }
        } else {
            stringBuilder = new StringBuilder(car.getNickname());
        }
        licencePlateView.getNickname().setText(stringBuilder.toString());
    }

    private void updateFormValues(CarForm carForm, Car car){

        carForm.getCarNumberPlaceHolder().setText(car.getCarNumber());
        carForm.getNicknamePlaceHolder().setText(car.getNickname());
        carForm.setEmojiID(car.getEmojiId());
    }

    @Override
    public void sendInput(int index) {

    }

    private void goToWaitingActivity(){

        Intent intent = new Intent(this,WaitingActivity.class);
        intent.putExtra("operation",1);
        intent.putExtra("email",emailPlaceHolder.getText().toString());
        intent.putExtra("password",passwordPlaceHolder.getText().toString());
        intent.putExtra("name",namePlaceHolder.getText().toString());
        startActivity(intent);
    }
}
