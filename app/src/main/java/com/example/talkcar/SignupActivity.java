package com.example.talkcar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private Button signInBtn;
    private ImageView addCar;
    private Database database;
    private int numOfCars = 0;
    private InputManager inputManager;


    FirebaseAuth mFirebaseAuth;
    Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setIds();
        inputManager = new InputManager();
        mFirebaseAuth = FirebaseAuth.getInstance();
        database = new Database();
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
        TextView carNumbernth = createTextView("Car number #" + ++numOfCars,18,Color.rgb(44,167,239));
        EditText carNumberPlaceHolder =  createEditText("Car Number",InputType.TYPE_CLASS_PHONE);
        inputManager.getAllCarNumbers().add(carNumberPlaceHolder);
        EditText nicknamePlaceHolder = createEditText("Nickname",InputType.TYPE_CLASS_TEXT);
        inputManager.getAllNickNames().add(nicknamePlaceHolder);
        TextView pickYourEmojiText = createTextView("Pick your emoji's car!",13,Color.BLACK);

        //Create emoji Container
        LinearLayout emojiContainer = new LinearLayout(this);
        emojiContainer.setOrientation(LinearLayout.HORIZONTAL);
        addEmojiToContainer(emojiContainer);
        addAllViewsToFormContainer(allFormContainer,carNumbernth,carNumberPlaceHolder,nicknamePlaceHolder,pickYourEmojiText,emojiContainer);


    }

    private void addAllViewsToFormContainer(LinearLayout allFormContainer,View... view) {

        for(View v: view){
            allFormContainer.addView(v);
        }
    }

    private void addEmojiToContainer(LinearLayout emojiContainer) {

        ImageView driverOne = createImageView(R.drawable.driver1);
        ImageView driverTwo = createImageView(R.drawable.driver2);
        ImageView driverThree = createImageView(R.drawable.driver3);

        setEmojiClickListeners(driverOne,driverTwo,driverThree);

        emojiContainer.addView(driverOne);
        emojiContainer.addView(driverTwo);
        emojiContainer.addView(driverThree);

    }

    private void setEmojiClickListeners(final ImageView driverOne, final ImageView driverTwo, final ImageView driverThree) {

        driverOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markEmoji(driverOne,Color.rgb(44,167,239));
                markEmoji(driverTwo, Color.WHITE);
                markEmoji(driverThree, Color.WHITE);
            }
        });

        driverTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo,Color.rgb(44,167,239));
                markEmoji(driverThree, Color.WHITE);
            }
        });

        driverThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                markEmoji(driverOne, Color.WHITE);
                markEmoji(driverTwo, Color.WHITE);
                markEmoji(driverThree,Color.rgb(44,167,239));
            }
        });
    }

    private ImageView createImageView(int image) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(200, 200);
        lp.setMargins(100,20,0,0);
        lp.gravity = Gravity.CENTER;
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(image);
        imageView.setLayoutParams(lp);

        return imageView;
    }

    private TextView createTextView(String text,int size, int color) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(910, 130);
        lp.setMargins(0,50,0,0);
        lp.gravity = Gravity.CENTER;
        TextView textView = new TextView(this);
        textView.setTextSize(size);
        textView.setText(text);
        textView.setTypeface(Typeface.create("sans-serif-smallcaps", Typeface.NORMAL));
        textView.setTextColor(color);
        textView.setLayoutParams(lp);

        return textView;
    }

    private EditText createEditText(String hint,int type) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(900, 130);
        lp.setMargins(0,30,20,20);
        lp.gravity = Gravity.CENTER;
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setTextSize(13);
        editText.setPadding(40,0,0,0);
        editText.setTypeface(Typeface.create("sans-serif-smallcaps", Typeface.NORMAL));
        editText.setHintTextColor(Color.BLACK);
        editText.setTextColor(Color.BLACK);
        editText.setBackgroundResource(R.drawable.edit_text_shape);
        editText.setInputType(type);
        editText.setLayoutParams(lp);

        return editText;

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
            String carNumber = inputManager.getAllCarNumbers().get(i).toString();
            String nickName = inputManager.getAllNickNames().get(i).toString();
            driver.addCar(new Car(carNumber, nickName,1));
        }
        database.saveDriver(driver);
    }

    private void setIds() {

        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        signInBtn = (Button)findViewById(R.id.signin_btn);
        addCar = (ImageView)findViewById(R.id.plus_sign);

    }

}
