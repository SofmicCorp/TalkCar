package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        mFirebaseAuth = FirebaseAuth.getInstance();
        databaseRef = new Database(new MD5());

        int operation = getIntent().getIntExtra("operation",0);
        String firstname = getIntent().getStringExtra("firstname");
        String lastname = getIntent().getStringExtra("lastname");
        String email = getIntent().getStringExtra("email");
        String password = getIntent().getStringExtra("password");

        switch (operation){
            case AUTH:
                authenticate(email,password);
                break;
            case SIGN:
                singUp(email,password, firstname, lastname);
                break;
        }
    }

    public void authenticate(final String email, String password){

        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(WaitingActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    goToLoginActivity();
                }else {
                    //finding the current driver in database
                    databaseRef.updateCurrentDriverByEmail(email, new OnGetDataListener(){

                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
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

    public void singUp(final String email, String password, final String firstname, final String lastname){

        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(WaitingActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    goToMainActivity();
                }else{
                    saveDriverToDatabase(firstname, lastname, email);
                    Intent intent = new Intent(WaitingActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void saveDriverToDatabase(String firstname, String lastname, String email){

        Driver driver = new Driver(firstname, lastname,email);
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
}

