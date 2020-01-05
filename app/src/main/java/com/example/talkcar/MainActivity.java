package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;


public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView takePhoto;
    private Bitmap imageBitmap;
    private CarNumberChecker carNumberChecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setIds();
        setClickListeners();
        carNumberChecker = new CarNumberChecker();
    }

    private void setIds(){

        takePhoto = (ImageView)findViewById(R.id.take_photo);
    }

    private void setClickListeners(){

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dispatchTakePictureIntent();
            }
        });
    }



    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.add:
                Toast.makeText(this, "Item 1 clicked", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return false;
        }
    }

    public void showPopup(View view) {

        PopupMenu popup = new PopupMenu(this,view);
        popup.setOnMenuItemClickListener(this);
        popup.inflate(R.menu.popup_menu);
        popup.show();
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
        }

        detectTextFromImage();
    }

    private void detectTextFromImage() {

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextRecognizer firebaseVisionTextRecognizer = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        firebaseVisionTextRecognizer.processImage(firebaseVisionImage).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {

                getTheDetectedTextFromImage(firebaseVisionText);

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
                if(carNumberChecker.isValidCarNumber(text)){
                    Toast.makeText(this, "car number is " + text, Toast.LENGTH_SHORT).show();
                    return;
                } 
            }
            Toast.makeText(this, "car number was not found. try to take another picture.", Toast.LENGTH_SHORT).show();
        }
    }


}
