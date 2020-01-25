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
                    goToLoginActivity();
                }else {
                    //finding the current driver in database
                    databaseRef.searchDriverByEmail(email, new OnGetDataListener(){
                        @Override
                        public void onSuccess(Driver driver) {
                            if(getIntent().getBooleanExtra("autologin",false)){
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

    public void goToLoginActivity(){

        Intent intent = new Intent(this,LoginActivity.class);
        intent.putExtra("fail", 1); //failed on authincate
        startActivity(intent);

    }

    private void goToMainActivity(){

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void singUp(final String email, String password, final String name){

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(WaitingActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    goToMainActivity();
                }else{
                    saveCurrentApplicationUserDetailsToSharedPreferences(email);
                    saveDriverToDatabase(name, email);
                    Intent intent = new Intent(WaitingActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void saveDriverToDatabase(String name, String email){

        Driver driver = new Driver(name,email);
        for(int i = 0; i < CarForm.allForms.size(); i++){
            String carNumber = CarForm.allForms.get(i).getCarNumberPlaceHolder().getText().toString();
            String nickName = CarForm.allForms.get(i).getNicknamePlaceHolder().getText().toString();
            String emojiId = CarForm.allForms.get(i).getEmojiID();
            Log.d("buba", "car number: " + carNumber);
            driver.addCar(new Car(carNumber, nickName,emojiId));
        }
        databaseRef.saveDriver(driver);

        //Set current driver on app
        ApplicationModel.setCurrentDriver(driver);
    }

    private void saveCurrentApplicationUserDetailsToSharedPreferences(final String email){

        sharedPreferences.edit().putBoolean("logged",true).apply();
        sharedPreferences.edit().putString("email",email).apply();

    }
}

