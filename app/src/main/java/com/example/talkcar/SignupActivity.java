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

public class SignupActivity extends AppCompatActivity {

    private EditText emailPlaceHolder;
    private EditText passwordPlaceHolder;
    private EditText carNumberPlaceHolder;
    private Button signInBtn;
    private ImageView addCar;
    private Database database;
    private int numOfCars = 0;



    FirebaseAuth mFirebaseAuth;
    Driver driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setIds();
        mFirebaseAuth = FirebaseAuth.getInstance();
        database = new Database();
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
                createAddCarForm();
            }
        });
//
//        emojiOne.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                emojiOneWasPicked = true;
//                emojiTwoWasPicked = false;
//                emojiThreeWasPicked = false;
//                showPickedEmoji();
//            }
//        });
//
//        emojiTwo.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//                emojiOneWasPicked = false;
//                emojiTwoWasPicked = true;
//                emojiThreeWasPicked = false;
//                showPickedEmoji();
//            }
//        });
//
//        emojiThree.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View v) {
//
//                emojiOneWasPicked = false;
//                emojiTwoWasPicked = false;
//                emojiThreeWasPicked = true;
//                showPickedEmoji();
//            }
//        });
//
//
//    }
//
//    private void showPickedEmoji() {
//
//        if(emojiOneWasPicked){
//
//            markEmoji(emojiOne);
//
//        } else{
//            emojiOne.setBackgroundColor(Color.HSVToColor(new float[]{0, 0, 100}));
//        }
//
//        if(emojiTwoWasPicked){
//
//            markEmoji(emojiTwo);
//
//        }else{
//
//            emojiTwo.setBackgroundColor(Color.HSVToColor(new float[]{0, 0, 100}));
//
//
//        }
//        if(emojiThreeWasPicked){
//
//            markEmoji(emojiThree);
//
//        }else {
//            emojiThree.setBackgroundColor(Color.HSVToColor(new float[]{0, 0, 100}));
//        }
    }

    private void createAddCarForm() {

        LinearLayout allFormContainer = (LinearLayout)findViewById(R.id.all_forms_container);
        TextView carNumbernth = createTextView("Car number #" + ++numOfCars);
        EditText carNumberPlaceHolder =  createEditText("Car Number");
        EditText nicknamePlaceHolder = createEditText("Nickname");
        TextView pickYourEmojiText = createTextView("Pick your emoji's car!");

        //
        LinearLayout emojiContainer = new LinearLayout(this);
        emojiContainer.setOrientation(LinearLayout.HORIZONTAL);
        addEmojiToContainer(emojiContainer);


        allFormContainer.addView(carNumbernth);
        allFormContainer.addView(carNumberPlaceHolder);
        allFormContainer.addView(nicknamePlaceHolder);
        allFormContainer.addView(pickYourEmojiText);
        allFormContainer.addView(emojiContainer);





    }

    private void addEmojiToContainer(LinearLayout emojiContainer) {

        ImageView driverOne = createImageView(R.drawable.driver1);
        ImageView driverTwo = createImageView(R.drawable.driver2);
        ImageView driverThree = createImageView(R.drawable.driver3);

        emojiContainer.addView(driverOne);
        emojiContainer.addView(driverTwo);
        emojiContainer.addView(driverThree);

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

    private TextView createTextView(String text) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(930, 130);
        lp.setMargins(0,20,0,0);
        lp.gravity = Gravity.CENTER;
        TextView textView = new TextView(this);
        textView.setTextSize(13);
        textView.setText(text);
        textView.setTypeface(Typeface.create("sans-serif-smallcaps", Typeface.NORMAL));
        textView.setTextColor(Color.BLACK);
        textView.setLayoutParams(lp);

        return textView;
    }

    private EditText createEditText(String hint) {

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(930, 130);
        lp.setMargins(0,0,20,20);
        lp.gravity = Gravity.CENTER;
        EditText editText = new EditText(this);
        editText.setHint(hint);
        editText.setTextSize(13);
        editText.setTypeface(Typeface.create("sans-serif-smallcaps", Typeface.NORMAL));
        editText.setHintTextColor(Color.BLACK);
        editText.setTextColor(Color.BLACK);
        editText.setBackgroundResource(R.drawable.edit_text_shape);
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        editText.setLayoutParams(lp);

        return editText;

    }

    private void markEmoji(ImageView emoji) {

        GradientDrawable gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(100);
        gd.setStroke(4, Color.rgb(44,167,239));
        emoji.setBackgroundDrawable(gd);
    }


    private void createFireBaseUser(){
        final String email = emailPlaceHolder.getText().toString().trim();
        String pwd = passwordPlaceHolder.getText().toString().trim();
        final String carNumber = carNumberPlaceHolder.getText().toString();

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

                        driver = new Driver(email);
                        driver.addCar(new Car(carNumber,"barMobile",1));
                        database.saveDriver(driver);

                        Intent intent = new Intent(SignupActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }else{
            Toast.makeText(SignupActivity.this,"Error Occurred!", Toast.LENGTH_SHORT);
        }
    }

    private void setIds() {

        emailPlaceHolder = (EditText)findViewById(R.id.email_placeholder);
        passwordPlaceHolder = (EditText)findViewById(R.id.password_placeholder);
        signInBtn = (Button)findViewById(R.id.signin_btn);
        addCar = (ImageView)findViewById(R.id.plus_sign);

    }

}
