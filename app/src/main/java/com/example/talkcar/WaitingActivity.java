package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;

public class WaitingActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private Database databaseRef;
    private final int AUTH = 0;
    private final int SIGN = 1;
    private SharedPreferences sharedPreferences;
    private final String LOGIN_FILE = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseRef = new Database(new MD5());
        sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);

        int operation = getIntent().getIntExtra("operation",0);

        String name = getIntent().getStringExtra("name");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        switch (operation){
            case AUTH:
                authenticate(email,password);
                break;
            case SIGN:
                singUp(email,password, name);
                break;
        }
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
                    goToLoginActivity(1);
                }else {
                    //finding the current driver in database
                    databaseRef.searchDriverByEmail(email, new OnGetDataListener(){
                        @Override
                        public void onSuccess(Driver driver) {
                            if(getIntent().getBooleanExtra("autologin",false)){
                                ApplicationModel.setCurrentDriver(driver);
                                //Auto login is saved in file after authenticate the email and password
                                saveCurrentApplicationUserDetailsToSharedPreferences(email);
                            }
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

    private void automaticSignin(String email) {


        //finding the current driver in database
        databaseRef.searchDriverByEmail(email, new OnGetDataListener(){

            @Override
            public void onSuccess(Driver driver) {
                if(driver != null){
                    ApplicationModel.setCurrentDriver(driver);
                    goToMainActivity();
                } else {
                    sharedPreferences.edit().clear().apply(); //clear sp for some tests
                    goToLoginActivity(0);
                }
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

    public void goToLoginActivity(int ERROR){

        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("error", ERROR); //failed on authincate
        startActivity(intent);

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
                        Log.d("BUBA", "onComplete: weak_password");
                        gotToSignUpActivity(1);
                    }
                    // if user enters wrong password.
                    catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
                        Log.d("BUBA", "onComplete: malformed_email");
                        gotToSignUpActivity(2);
                    }
                    catch (FirebaseAuthUserCollisionException existEmail) {
                        Log.d("BUBA", "onComplete: exist_email");
                        gotToSignUpActivity(3);
                    }

                    catch (Exception e) {
                        Log.d("BUBA", "onComplete: " + e.getMessage());
                        gotToSignUpActivity(4);
                    }
            }else{
                    saveCurrentApplicationUserDetailsToSharedPreferences(email);
                    Driver driver = saveDriverToDatabase(name, email);
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



    private Driver saveDriverToDatabase(String name, String email){

        Driver driver = new Driver(name,email);
        for(int i = 0; i < CarForm.allForms.size(); i++){
            String carNumber = CarForm.allForms.get(i).getCarNumberPlaceHolder().getText().toString();
            String nickName = CarForm.allForms.get(i).getNicknamePlaceHolder().getText().toString();
            String emojiId = CarForm.allForms.get(i).getEmojiID();
            Log.d("buba", "car number: " + carNumber);
            driver.addCar(new Car(carNumber, nickName,emojiId));
        }
        databaseRef.saveDriver(driver);

        //return the driver that has been saved in database
        return driver;
    }

    private void saveCurrentApplicationUserDetailsToSharedPreferences(final String email){

        sharedPreferences.edit().putBoolean("logged",true).apply();
        sharedPreferences.edit().putString("email",email).apply();

    }
}

