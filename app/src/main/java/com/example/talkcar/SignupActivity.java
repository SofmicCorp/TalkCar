package com.example.talkcar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;

public class SignupActivity extends AppCompatActivity {

    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private Button signInBtn;
    private ImageView addCar;
    private int numOfCars = 0;
    private InputManager inputManager;
    private Database databaseRef;
    private HashMap<ImageView,Integer> indexes; // for saving the index of every emoji extra car form (
    private FirebaseAuth mFirebaseAuth;
    private Driver driver;
    private ArrayList<TextView> allFormHeadersTextViews;
    private DynamicallyXML dynamicallyXML;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setIds();
        inputManager = new InputManager();
        mFirebaseAuth = FirebaseAuth.getInstance();
        indexes = new HashMap<>();
        databaseRef = new Database();
        allFormHeadersTextViews = new ArrayList<>();
        dynamicallyXML = new DynamicallyXML();
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

        LinearLayout allFormContainer = (LinearLayout)findViewById(R.id.all_forms_container);

        LinearLayout formHeaderAndDeleteContainer = new LinearLayout(this);
        LinearLayout inputUserContainer = new LinearLayout(this);
        LinearLayout emojiContainer = new LinearLayout(this);

        inputUserContainer.setOrientation(LinearLayout.VERTICAL);
        formHeaderAndDeleteContainer.setOrientation(LinearLayout.HORIZONTAL);
        emojiContainer.setOrientation(LinearLayout.HORIZONTAL);

        TextView carNumbernth = dynamicallyXML.createTextView(this,"Car number #" + (numOfCars + 1),18,Color.rgb(44,167,239),Gravity.CENTER,100,50,0,0);
        ImageView deleteFormBtn = dynamicallyXML.createImageView(this,R.drawable.minussign,70,70,Gravity.CENTER,-200,25,0,0);
        allFormHeadersTextViews.add(carNumbernth);
        addAllViewsLayout(formHeaderAndDeleteContainer,carNumbernth,deleteFormBtn);

        EditText carNumberPlaceHolder =  dynamicallyXML.createEditText(this,"Car Number",InputType.TYPE_CLASS_PHONE);
        inputManager.getAllCarNumbers().add(carNumberPlaceHolder);
        EditText nicknamePlaceHolder = dynamicallyXML.createEditText(this,"Nickname",InputType.TYPE_CLASS_TEXT);
        inputManager.getAllNickNames().add(nicknamePlaceHolder);
        TextView pickYourEmojiText = dynamicallyXML.createTextView(this,"Pick your emoji's car!",13,Color.BLACK,Gravity.CENTER,220,50,0,0);
        addAllViewsLayout(inputUserContainer,carNumberPlaceHolder,nicknamePlaceHolder,pickYourEmojiText);

        //Create emoji Container
        addEmojiToContainer(emojiContainer);
        addAllViewsLayout(allFormContainer,formHeaderAndDeleteContainer,inputUserContainer,emojiContainer);
        numOfCars++;

        setFormListeners(deleteFormBtn,formHeaderAndDeleteContainer,inputUserContainer,emojiContainer);
    }

    private void setFormListeners(ImageView deleteFormBtn, final LinearLayout formHeaderAndDeleteContainer, final LinearLayout inputUserContainer, final LinearLayout emojiContainer ) {

        deleteFormBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteForm(formHeaderAndDeleteContainer, inputUserContainer,emojiContainer);
            }
        });
    }

    private void deleteForm(LinearLayout formHeaderAndDeleteContainer, LinearLayout inputUserContainer, LinearLayout emojiContainer) {

        formHeaderAndDeleteContainer.removeAllViews();
        inputUserContainer.removeAllViews();
        emojiContainer.removeAllViews();
        Log.d("SASA", "num of cars before removal: " + numOfCars);
        numOfCars--;
        Log.d("SASA", "num of cars: after removal " + numOfCars);
        allFormHeadersTextViews.remove(numOfCars);

        formHeaderAndDeleteContainer = null;
        inputUserContainer = null;
        emojiContainer = null;

        updateAllFormHeadersTextViews();
    }

    private void updateAllFormHeadersTextViews() {

        for(int i = 0; i < numOfCars; i++){
            allFormHeadersTextViews.get(i).setText("Car nuasdsfmber #" + (i + 1));
        }
    }

    private void addAllViewsLayout(LinearLayout layout,View... view) {

        for(View v: view){
            layout.addView(v);
        }
    }

    private void addEmojiToContainer(LinearLayout emojiContainer) {

        ImageView driverOne = dynamicallyXML.createImageView(this,R.drawable.driver1,200,200,Gravity.CENTER,100,20,0,0);
        ImageView driverTwo = dynamicallyXML.createImageView(this,R.drawable.driver2,200,200, Gravity.CENTER,100,20,0,0);
        ImageView driverThree = dynamicallyXML.createImageView(this,R.drawable.driver3,200,200,Gravity.CENTER,100,20,0,0);

        indexes.put(driverOne,numOfCars); // when we add more car form we add car and index to the indexes hashmap
                                            //driverOne is just a mark to know which form are we. it doesnt matter at all if
                                            //it will be driverOne or driverTwo or driverThree, it is just a mark for hashmap

        setEmojiClickListeners(driverOne,driverTwo,driverThree);
        addAllViewsLayout(emojiContainer,driverOne, driverTwo, driverThree);


    }

    private void setEmojiClickListeners(final ImageView driverOne, final ImageView driverTwo, final ImageView driverThree) {

        driverOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markEmoji(driverOne,Color.rgb(44,167,239));
                markEmoji(driverTwo, Color.WHITE);
                markEmoji(driverThree, Color.WHITE);

                try{
                    driverOne.setTag(1);
                    inputManager.getAllEmojisIds().set(indexes.get((driverOne)),driverOne.getTag().toString());
                } catch(IndexOutOfBoundsException e){
                    inputManager.getAllEmojisIds().add(driverOne.getTag().toString());
                }

            }
        });

        driverTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo,Color.rgb(44,167,239));
                markEmoji(driverThree, Color.WHITE);

                try{
                    driverTwo.setTag(2);
                    inputManager.getAllEmojisIds().set(indexes.get((driverOne)),driverTwo.getTag().toString());
                } catch(IndexOutOfBoundsException e){
                    inputManager.getAllEmojisIds().add(driverTwo.getTag().toString());
                }
            }
        });

        driverThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo, Color.WHITE);
                markEmoji(driverThree,Color.rgb(44,167,239));


                try{
                    driverThree.setTag(3);
                    inputManager.getAllEmojisIds().set(indexes.get((driverOne)),driverThree.getTag().toString());
                } catch(IndexOutOfBoundsException e){

                    inputManager.getAllEmojisIds().add(driverThree.getTag().toString());
                }
            }
        });
    }

    private void markEmoji(ImageView emoji,int color) {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(100);
        gd.setStroke(4, color);
        emoji.setBackgroundDrawable(gd);
    }


    private void createFireBaseUser(){
        final String email = emailPlaceHolder.getText().toString().trim();
        String pwd = passwordPlaceHolder.getText().toString().trim();

        if(email.isEmpty()){
            emailPlaceHolder.setError("Please enter email");
            emailPlaceHolder.requestFocus();
        }else if(pwd.isEmpty()){
            passwordPlaceHolder.setError("Please enter your password");
        }else if(email.isEmpty() && pwd.isEmpty()){
            Toast.makeText(SignupActivity.this,"Fields Are Empty!", Toast.LENGTH_SHORT);
        }else if(!(email.isEmpty() && pwd.isEmpty())){
            mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(SignupActivity.this,"Sign in unsuccessful, please try again!", Toast.LENGTH_SHORT);
                    }else{
                       saveDriverToDatabase(email);
                       Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                       startActivity(intent);
                    }
                }
            });
        }else{
            Toast.makeText(SignupActivity.this,"Error Occurred!", Toast.LENGTH_SHORT);
        }
    }

    private void saveDriverToDatabase(String email){

        driver = new Driver(email);
        for(int i = 0; i < numOfCars; i++){
            String carNumber = inputManager.getAllCarNumbers().get(i).getText().toString();
            String nickName = inputManager.getAllNickNames().get(i).getText().toString();
            String emojiId = inputManager.getAllEmojisIds().get(i);
            driver.addCar(new Car(carNumber, nickName,emojiId));
        }
        databaseRef.saveDriver(driver);

        //Set current driver on app
        LoginActivity.applicationModel.setCurrentDriver(driver);
    }

    private void setIds() {

        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        signInBtn = (Button)findViewById(R.id.signin_btn);
        addCar = (ImageView)findViewById(R.id.plus_sign);

    }

}
