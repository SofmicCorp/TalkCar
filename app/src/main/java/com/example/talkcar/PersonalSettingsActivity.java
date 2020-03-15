package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.talkcar.Cache.ApplicationModel;
import com.example.talkcar.Database.Database;
import com.example.talkcar.Driver.Driver;
import com.example.talkcar.Helpers.DynamicallyXML;
import com.example.talkcar.Helpers.FieldsChecker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class PersonalSettingsActivity extends AppCompatActivity {

    private EditText namePlaceHolder;
    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private Button finishEditBtn;
    private DynamicallyXML dynamicallyXML;
    private Context context;
    private LinearLayout personalSettingsContainer;
    private FieldsChecker fieldsChecker;
    private SharedPreferences sharedPreferences;
    private ProgressBar progressBar;
    private final String LOGIN_FILE = "login";



    private FirebaseAuth mFirebaseAuth;
    public static boolean isActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_settings);
        dynamicallyXML = new DynamicallyXML();
        sharedPreferences = getSharedPreferences(LOGIN_FILE,MODE_PRIVATE);
        context = this;
        fieldsChecker = new FieldsChecker();
        setIds();
        setClickListeners();
    }

    private void setClickListeners() {

        finishEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar = dynamicallyXML.createProgressBar(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(100,100);
                params.gravity = Gravity.CENTER;
                personalSettingsContainer.addView(progressBar,params);

                if(!fieldsChecker.checkUserDetailsFields(namePlaceHolder,emailPlaceHolder,passwordPlaceHolder)){
                    personalSettingsContainer.removeView(progressBar);
                    return;
                }

                changeDriverEmail();
            }
        });
    }

    private void changeDriverEmail() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        // Get auth credentials from the user for re-authentication
        AuthCredential credential = EmailAuthProvider
                .getCredential(ApplicationModel.getCurrentDriver().getEmail(), passwordPlaceHolder.getText().toString()); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(!task.isSuccessful()){
                            passwordPlaceHolder.setError("Password is incorrect");
                            personalSettingsContainer.removeView(progressBar);
                            return;
                        }

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(emailPlaceHolder.getText().toString().trim())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if(!task.isSuccessful()){
                                            handleFailedDetailsUpdate(task);
                                        }
                                        else  {
                                            saveDriverWithNewDetails();
                                            updateSharedPrefernceFile();
                                            goToSettingsActivity();
                                        }
                                    }
                                });
                        }
                });
    }


    private void handleFailedDetailsUpdate(Task<Void> task){

        try {
            throw task.getException();
        }

        // if user enters wrong password.
        catch (FirebaseAuthInvalidCredentialsException malformedEmail) {
            emailPlaceHolder.setError("Illegal email was inserted ");
            personalSettingsContainer.removeView(progressBar);
        }
        catch (FirebaseAuthUserCollisionException existEmail) {
            emailPlaceHolder.setError("Email is already exists ");
            personalSettingsContainer.removeView(progressBar);
        }

        catch (Exception e) {
            personalSettingsContainer.removeView(progressBar);
        }

    }

    private void updateSharedPrefernceFile() {

        sharedPreferences.edit().clear().apply();
        sharedPreferences.edit().putBoolean("logged",true).apply();
        sharedPreferences.edit().putString("email",emailPlaceHolder.getText().toString()).apply();

    }

    private void saveDriverWithNewDetails() {

        Driver driver = new Driver(namePlaceHolder.getText().toString(),emailPlaceHolder.getText().toString(),FirebaseAuth.getInstance().getCurrentUser().getUid());
        driver.setCars(ApplicationModel.getCurrentDriver().getCars());
        Database.saveDriver(driver,driver.getuId());
        ApplicationModel.setCurrentDriver(driver);
    }

    private void goToSettingsActivity() {

        SettingsActivity.activity.finish();
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
        finish();
    }

    private void setIds() {

        personalSettingsContainer = (LinearLayout)findViewById(R.id.personal_settings_container);
        namePlaceHolder = (EditText)findViewById(R.id.name_holder);
        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        finishEditBtn = (Button)findViewById(R.id.finish_edit_btn);

        namePlaceHolder.setText(ApplicationModel.getCurrentDriver().getName());
        emailPlaceHolder.setText(ApplicationModel.getCurrentDriver().getEmail());
    }
}
