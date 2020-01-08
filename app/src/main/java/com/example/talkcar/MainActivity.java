package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView takePhoto;
    private ImageView carPicker;
    private Bitmap imageBitmap;
    private CarNumberChecker carNumberChecker;
    private String carNumber;
    private Driver driver;
    private Database databaseRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseRef = new Database();
        setIds();
        setClickListeners();
        carNumberChecker = new CarNumberChecker();
        updateCarPickerIcon(0);
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
                
                createListOfCarsPopup();
            }
        });
    }

    private void createListOfCarsPopup() {

        ListAdapter adapter = new ArrayAdapter<Car>(
                this, android.R.layout.select_dialog_item, android.R.id.text1, driver.getCars()){
                 public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                tv.setTypeface(Typeface.create("sans-serif-smallcaps", Typeface.BOLD));

                //call setBounds with the required size and then call setCompoundDrawables

                //Put the image on the TextView
                if(driver.getCars().get(position).getEmojiId().equals("1")) {

                    createImageToPopup(tv,R.drawable.driver1);

                } else if(driver.getCars().get(position).getEmojiId().equals("2")){

                    createImageToPopup(tv,R.drawable.driver2);
                }
                else {
                    createImageToPopup(tv,R.drawable.driver3);
                }

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);
                return v;
            }
        };

        new AlertDialog.Builder(this)
                .setTitle("PICK A CAR")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //... do somthing when click...
                        changeCurrentCar(item);
                    }
                }).show();

    }

    private void changeCurrentCar(int index) {
        LoginActivity.applicationModel.setCurrentCar(driver.getCars().get(index));
        updateCarPickerIcon(index);
    }

    private void createImageToPopup(TextView tv, int image){

        Drawable img = ContextCompat.getDrawable(MainActivity.this,image);
        img.setBounds(-30, 0, 180, 150);
        tv.setCompoundDrawables(img, null, null, null);
    }

    private void startChatWithAnotherCar() {

        if(carNumber != null) {
            carNumber = carNumberChecker.removeAllTokensFromCarNumber(carNumber);
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
        } else
            return;

        detectTextFromImage();
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
                if(carNumberChecker.isValidCarNumber(text)) {
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
}
