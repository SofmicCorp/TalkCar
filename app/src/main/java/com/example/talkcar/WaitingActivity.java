package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        mFirebaseAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);

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

        Log.d("BIBI", "Im in Authentticate!!!!!!!!: ");

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
                            Log.d("BIBI", "Waiting Activity : authincate: ");
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

        Log.d("BIBI", "WaitingActivity automaticSignin: begin of function ");

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
                    Log.d("PUKI", "driver: " + driver);
                    ApplicationModel.setCurrentDriver((Driver)driver);
                    Log.d("BIBI", "Waiting Activity : automaticSignin mor!!!: ");
                    goToMainActivity();

                } else {
                    Toast.makeText(WaitingActivity.this, "Car was not found in the system...", Toast.LENGTH_SHORT).show();
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
        finish();

    }

    private void goToMainActivity(){

        Log.d("BIBI", "Waiting Activity : goToMainActivity: ");
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
                    Driver driver = saveDriverToDatabase(name,email, FirebaseAuth.getInstance().getCurrentUser().getUid());
                    addUidToAllDriversCars(driver);
                    //Set current driver on app
                    ApplicationModel.setCurrentDriver(driver);
                    Log.d("BIBI", "WaitingActiviy : signUp: ");
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
            Log.d("buba", "car number: " + carNumber);
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

