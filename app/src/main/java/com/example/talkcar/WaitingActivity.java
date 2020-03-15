package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.talkcar.Cache.ApplicationModel;
import com.example.talkcar.Cars.Car;
import com.example.talkcar.Cars.CarForm;
import com.example.talkcar.Database.Database;
import com.example.talkcar.Driver.Driver;
import com.example.talkcar.Interfaces.OnGetDataListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class WaitingActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private Database databaseRef;
    private final int AUTH = 0;
    private final int SIGN = 1;
    public static Activity actvitiy;
    private SharedPreferences sharedPreferences;
    private final String LOGIN_FILE = "login";
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        mFirebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);
        actvitiy = this;

        int operation = getIntent().getIntExtra("operation",0);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        if(email != null && name != null){
            email = email.trim();
            name = name.trim();
        }

        switch (operation){
            case AUTH:
                authenticate(email,password);
                break;
            case SIGN:
                singUp(email,password, name);
                break;
        }
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

    public void authenticate(final String email,final String password){

        if(sharedPreferences.getBoolean("logged",false)){
            //If the user is allready have been in the app once, it will autologin his user/
            //so load the data for that user is here.
            String saveEmail = sharedPreferences.getString("email",null);
            automaticSignin(saveEmail);
            return;
        }

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(WaitingActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(WaitingActivity.this, "Email or password is incorrect", Toast.LENGTH_SHORT).show();
                    goToLoginActivity(1);
                }else {
                    //finding the current driver in database
                    databaseRef.searchDriverByUid(FirebaseAuth.getInstance().getCurrentUser().getUid(), new OnGetDataListener(){
                        @Override
                        public void onSuccess(Object driver) {
                            if(getIntent().getBooleanExtra("autologin",false)){
                                ApplicationModel.setCurrentDriver((Driver)driver);
                                //Auto login is saved in file after authenticate the email and password
                                saveCurrentApplicationUserDetailsToSharedPreferences(email);
                            }
                            goToMainActivity();
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            }
        });
    }

    private void automaticSignin(String email) {

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            //Check if user is exists in data base
            sharedPreferences.edit().clear().apply(); //clear sp for some tests
            goToLoginActivity(0);
            return;
        }

        //finding the current driver in database
        databaseRef.searchDriverByUid(FirebaseAuth.getInstance().getCurrentUser().getUid(), new OnGetDataListener(){

            @Override
            public void onSuccess(Object driver) {
                if(driver != null){
                    ApplicationModel.setCurrentDriver((Driver)driver);
                    goToMainActivity();

                } else {
                    Toast.makeText(WaitingActivity.this, "Car was not found in the system...", Toast.LENGTH_SHORT).show();
                    sharedPreferences.edit().clear().apply(); //clear sp for some tests
                    goToLoginActivity(0);
                }

            }
            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    public void goToLoginActivity(int ERROR){

        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("error", ERROR); //failed on authincate
        startActivity(intent);
        finish();

    }

    private void goToMainActivity(){

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void gotToSignUpActivity(int ERROR){

        Intent intent = new Intent(this,SignupActivity.class);
        intent.putExtra("error", ERROR); //failed on signup
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();

    }

    public void singUp(final String email, final String password, final String name){
        //Sign up!
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(WaitingActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        throw task.getException();
                    }
                    // if user enters wrong email.
                    catch (FirebaseAuthWeakPasswordException weakPassword) {
                        gotToSignUpActivity(1);
                    }
                    // if user enters wrong password.
                    catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                        gotToSignUpActivity(2);
                    }
                    catch (FirebaseAuthUserCollisionException existEmail) {
                        gotToSignUpActivity(3);
                    }

                    catch (Exception e) {
                        gotToSignUpActivity(4);
                    }
            }else{
                    saveCurrentApplicationUserDetailsToSharedPreferences(email);
                    Driver driver = saveDriverToDatabase(name,email, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    addUidToAllDriversCars(driver);
                    //Set current driver on app
                    ApplicationModel.setCurrentDriver(driver);
                    goToMainActivity();
                    SignupActivity.activity.finish();
                    LoginActivity.activity.finish();
                    finish();
                }
            }
        });
    }

    private void addUidToAllDriversCars(Driver driver) {

        for(int i = 0; i < driver.getCars().size(); i++){
            driver.getCars().get(i).setDriverUid(driver.getuId());
        }
    }


    private Driver saveDriverToDatabase(String name,String email ,String uId){

        Driver driver = new Driver(name,email,uId);
        for(int i = 0; i < CarForm.allForms.size(); i++){
            String carNumber = CarForm.allForms.get(i).getCarNumberPlaceHolder().getText().toString();
            String nickName = CarForm.allForms.get(i).getNicknamePlaceHolder().getText().toString();
            String emojiId = CarForm.allForms.get(i).getEmojiID();
            driver.addCar(new Car(carNumber, nickName,emojiId,uId));
        }
        databaseRef.saveDriver(driver,FirebaseAuth.getInstance().getCurrentUser().getUid());

        //return the driver that has been saved in database
        return driver;
    }

    private void saveCurrentApplicationUserDetailsToSharedPreferences(final String email){

        sharedPreferences.edit().putBoolean("logged",true).apply();
        sharedPreferences.edit().putString("email",email).apply();

    }
}

