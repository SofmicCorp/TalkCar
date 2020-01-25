package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;


//For Future Sarel And Mor!
//There is one really important thing about how this system works.
//When we sign up - first we create for each car a form
//Form include data about the car. The form where you pick car number, nickname and emoji
//After the form is created we create the "GUI" for those details - which is the yellow
//Licenece plate.
//When you are in Main Activity - we always delete the arrays of all forms and all car views first
//only for the one scanrio when user go to main activity from sign up
//Then we fill the array with the data from the current driver database (that is why we delete it, we dont want them to be
//משוכפלים
//As we said above for each CarView we need a form- they are highly copuled!
//So we create a forms array withthe data of the current drives
//And we create CarsViews array from with the data of the current drivers!


public class MainActivity extends AppCompatActivity implements OnInputListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView takePhoto;
    private ImageView carPicker;
    private ImageView settings;
    private ImageView addCarBtn;
    private ImageView shine;
    private Bitmap imageBitmap;
    private FieldsChecker fieldsChecker;
    private String carNumber;
    private Driver driver;
    private Database databaseRef;
    private DynamicallyXML dynamicallyXML;
    private Effects effects;
    private Handler handler;
    private Runnable runnable;
    public static Activity activity;
    private final int DELAY = 3*1000; //Delay for 3 seconds.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseRef = new Database(new MD5());
        dynamicallyXML = new DynamicallyXML();
        effects = new Effects();
        handler = new Handler();
        MainActivity.activity = this;
        setIds();
        setClickListeners();
        fieldsChecker = new FieldsChecker();
        updateCarPickerIcon(0);

        //Clean Forms and Car Views
        CarForm.removeAllForms();
        CarView.removeAllCarViews();

        //Create Forms And Card Views
        CarForm.createFormsFromCars(ApplicationModel.getCurrentDriver().getCars(),this);
        createCarViewsFromCars();

        //Script check
//
//        databaseRef.databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                Car car = dataSnapshot.child("33226de4860fdea8c3496bd151553756").child("cars").getValue(Car.class);
//                Log.d("BUBA", "onDataChange: " + car.getCarNumber());
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

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

        driver = ApplicationModel.getCurrentDriver();
        ApplicationModel.setCurrentCar(driver.getCars().get(index));

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
        settings = (ImageView)findViewById(R.id.settings);
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

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSettings();

            }
        });
    }

    private void openSettings() {

        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.enter, R.anim.exit);
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
        ApplicationModel.setCurrentCar(driver.getCars().get(index));
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
            databaseRef.searchCarByCarNumber(carNumber, new OnGetDataListener() {
                @Override
                public void onSuccess(Driver driver) {

                    if(ApplicationModel.getLastCarNumberSearch() != null)
                        openChat(ApplicationModel.getLastCarNumberSearch());
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
            //if you are here it means we iterate all elements in for loop and no car number is valid.
            Toast.makeText(this, "car number was not found. try to take another picture.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openChat(String carNumber) {

        Toast.makeText(this, "chat has been open with " + carNumber, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void sendInput(Car car) {

        LinearLayout container = new LinearLayout(this);//fake container;

        Driver driver = ApplicationModel.getCurrentDriver();
        driver.addCar(car);

        //get the new car details and create a card view to that car
        TextView nickname = dynamicallyXML.createTextView(this,car.getNickname(),40, Color.BLACK, Gravity.CENTER,20,50,10,10);
        CarView card = new CarView(nickname, CarForm.allForms.size() - 1 ,container,this,this,car.getCarNumber());
        CarView.allCarViews.add(card);
        //Save car to database
        databaseRef.saveDriver(ApplicationModel.getCurrentDriver());
    }

    @Override
    public void sendInputToEdit(Car newCar, CarView carView, CarForm carForm) {

        //Update data base and update all car views.
        ApplicationModel.getCurrentDriver().getCars().get(carView.getCardId()).setCarNumber(newCar.getCarNumber());
        ApplicationModel.getCurrentDriver().getCars().get(carView.getCardId()).setNickname(newCar.getNickname());
        ApplicationModel.getCurrentDriver().getCars().get(carView.getCardId()).setEmojiId(newCar.getEmojiId());
        databaseRef.saveDriver(driver);

        updateCarView(driver.getCars().get(carView.getCardId()),carView.getCardId());
        updateCarPickerIcon(carView.getCardId());

    }

    private void updateCarView(Car newCar, int index) {

        StringBuilder stringBuilder;

        if(newCar.getNickname().equals(newCar.getCarNumber())){
            stringBuilder = FieldsChecker.addDashes(newCar.getNickname());
        } else{
            stringBuilder = new StringBuilder(newCar.getNickname());
        }

        CarView.allCarViews.get(index).getNickname().setText(stringBuilder);
    }

    public void createCarViewsFromCars(){

        StringBuilder carNickname;
        LinearLayout container = new LinearLayout(this); // fake container

        //Set the array of car views
        for(int i = 0; i < driver.getCars().size(); i++) {

            Car car = driver.getCars().get(i);
            if (car.getNickname().isEmpty()) {
                carNickname = FieldsChecker.addDashes(car.getNickname());
            } else {
                carNickname = new StringBuilder(car.getNickname());
            }
            TextView nickname = dynamicallyXML.createTextView(this, carNickname.toString(), 40, Color.BLACK, Gravity.CENTER, 20, 50, 10, 10);
            final CarView carView = new CarView(nickname, i, container, this, MainActivity.activity, car.getCarNumber());

            CarForm.allForms.get(i).getCarNumberPlaceHolder().setText(car.getCarNumber());
            CarForm.allForms.get(i).getNicknamePlaceHolder().setText(car.getNickname());
            CarForm.allForms.get(i).setEmojiID(car.getEmojiId());

            CarView.allCarViews.add(carView);
        }
    }

    @Override
    public void sendInput(int index) {
        changeCurrentCar(index);
    }
}
