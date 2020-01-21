package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;


public class MainActivity extends AppCompatActivity implements OnInputListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView takePhoto;
    private ImageView carPicker;
    private Bitmap imageBitmap;
    private FieldsChecker fieldsChecker;
    private String carNumber;
    private Driver driver;
    private Database databaseRef;
    private DynamicallyXML dynamicallyXML;
    private ImageView addCarBtn;
    private ImageView shine;
    private Effects effects;
    private Handler handler;
    private Runnable runnable;
    private final int DELAY = 3*1000; //Delay for 3 seconds.  One second = 1000 milliseconds.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseRef = new Database(new MD5());
        dynamicallyXML = new DynamicallyXML();
        effects = new Effects();
        handler = new Handler();
        setIds();
        setClickListeners();
        fieldsChecker = new FieldsChecker();
        updateCarPickerIcon(0);
    }

    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                effects.shine(takePhoto,shine);

                handler.postDelayed(runnable, DELAY);
            }
        }, DELAY);

        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    private void updateCarPickerIcon(int index) {

        driver = LoginActivity.applicationModel.getCurrentDriver();
        LoginActivity.applicationModel.setCurrentCar(driver.getCars().get(index));

        switch(driver.getCars().get(index).getEmojiId()){
            case "1":
                carPicker.setImageResource(R.drawable.driver1);
                break;
            case "2":
                carPicker.setImageResource(R.drawable.driver2);
                break;
            case "3":
                carPicker.setImageResource(R.drawable.driver3);
                break;
            default:
        }
    }

    private void setIds(){

        takePhoto = (ImageView)findViewById(R.id.take_photo);
        carPicker = (ImageView)findViewById(R.id.car_picker);
        addCarBtn = (ImageView) findViewById(R.id.add_car_btn);
        shine = (ImageView)findViewById(R.id.shine);

    }

    private void setClickListeners(){

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });
        
        carPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                openListOfCarsDialog();
            }
        });

        addCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openNewCarDialog();
            }
        });
    }

    private void openListOfCarsDialog() {

        CarPickerDialog dialog = new CarPickerDialog();
        dialog.show(getSupportFragmentManager(),"CarPickerDialog");

    }


    private void openNewCarDialog() {

        AddNewCarDialog dialog = new AddNewCarDialog();
        dialog.show(getSupportFragmentManager(),"AddNewCarDialog");
    }


    private void changeCurrentCar(int index) {
        LoginActivity.applicationModel.setCurrentCar(driver.getCars().get(index));
        updateCarPickerIcon(index);
    }

    private void createImageToPopup(TextView tv, int carImgId, int editIconId){

        Drawable carIcon = ContextCompat.getDrawable(MainActivity.this,carImgId);
        Drawable  editIcon = ContextCompat.getDrawable(MainActivity.this,editIconId);
        carIcon.setBounds(-30, 0, 180, 150);
        editIcon.setBounds(0,0,100,100);
        tv.setCompoundDrawables(carIcon, null, editIcon, null);
    }

    private void startChatWithAnotherCar() {

        if(carNumber != null) {
            carNumber = fieldsChecker.removeAllTokensFromCarNumber(carNumber);
            //Check if car number is in database, and if it does, open a conversation!
            databaseRef.updateLastCarNumberSearch(carNumber, new OnGetDataListener() {
                @Override
                public void onSuccess(DataSnapshot dataSnapshot) {

                    if(LoginActivity.applicationModel.getLastCarNumberSearch() != null)
                        openChat(LoginActivity.applicationModel.getLastCarNumberSearch());
                    else
                        Toast.makeText(MainActivity.this, "Car was not found in the system...", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onStart() {
                    Log.d("CHECK", "Wait for data... ");
                }

                @Override
                public void onFailure() {
                    Toast.makeText(MainActivity.this, "Somthing went wrong...", Toast.LENGTH_SHORT).show();

                }
            });

        }
    }

    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            detectTextFromImage();
        }
    }

    private void detectTextFromImage() {

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                getTheDetectedTextFromImage(firebaseVisionText);
                startChatWithAnotherCar();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ERROR", "onFailure: " + e);
            }
        });
    }

    private void getTheDetectedTextFromImage(FirebaseVisionText firebaseVisionText) {

        List<FirebaseVisionText.TextBlock> blockList = firebaseVisionText.getTextBlocks();
        if(blockList.size() == 0){
            Toast.makeText(this, "car number was not found. try to take another picture.", Toast.LENGTH_SHORT).show();
        } else {
            for(FirebaseVisionText.TextBlock block: firebaseVisionText.getTextBlocks()){
                String text =  block.getText();
                //Check if car number is valid
                if(fieldsChecker.isValidCarNumber(text)) {
                    carNumber = text;
                    return;
                }
            }
            //if you are here it means we iterate all elements in foor loop and no car number is valid.
            Toast.makeText(this, "car number was not found. try to take another picture.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openChat(String carNumber) {

        Toast.makeText(this, "chat has been open with " + carNumber, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendInput(Car car) {

        Driver driver = LoginActivity.applicationModel.getCurrentDriver();
        driver.addCar(car);
        //Save car to database
        databaseRef.saveDriver(LoginActivity.applicationModel.getCurrentDriver());
    }

    @Override
    public void sendInput(int index) {
        changeCurrentCar(index);
    }
}
