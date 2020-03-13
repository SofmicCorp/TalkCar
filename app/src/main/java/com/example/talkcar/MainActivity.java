package com.example.talkcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaPlayer;
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

import com.example.talkcar.Cache.ApplicationModel;
import com.example.talkcar.Cars.AddNewCarDialog;
import com.example.talkcar.Cars.Car;
import com.example.talkcar.Cars.CarForm;
import com.example.talkcar.Cars.CarPickerDialog;
import com.example.talkcar.Cars.LicencePlateView;
import com.example.talkcar.Chats.ChattedCarsMap;
import com.example.talkcar.Chats.Message;
import com.example.talkcar.Database.Database;
import com.example.talkcar.Driver.Driver;
import com.example.talkcar.Helpers.DynamicallyXML;
import com.example.talkcar.Helpers.Effects;
import com.example.talkcar.Helpers.FieldsChecker;
import com.example.talkcar.Interfaces.OnGetDataListener;
import com.example.talkcar.Interfaces.OnInputListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.HashMap;
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
//As we said above for each LicencePlateView we need a form- they are highly copuled!
//So we create a forms array withthe data of the current drives
//And we create CarsViews array from with the data of the current drivers!


public class MainActivity extends AppCompatActivity implements OnInputListener {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView takePhoto;
    private static ImageView carPicker;
    private ImageView settings;
    private ImageView addCarBtn;
    private ImageView shine;
    private static ImageView chatsView;
    private static Context context;
    public static HashMap<String,Integer> emojiMap;
    public static HashMap<String, Message> chatKeyLastMessageMap;
    private Bitmap imageBitmap;
    private FieldsChecker fieldsChecker;
    private String chattedCarNumber;
    private DynamicallyXML dynamicallyXML;
    private Effects effects;
    private Handler handler;
    private Runnable runnable;
    public static boolean someMessageWereNotRead;
    public static Activity activity;
    public static boolean isActive;
    private static MediaPlayer beepSound;
    private final int DELAY = 3*1000; //Delay for 3 seconds.


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dynamicallyXML = new DynamicallyXML();
        effects = new Effects();
        handler = new Handler();
        MainActivity.activity = this;
        MainActivity.someMessageWereNotRead = false;
        context = this;
        setIds();
        setSounds();
        setClickListeners();
        checkIfAllMessagesWereRead();
        initEmojiMap();
        fieldsChecker = new FieldsChecker();
        ApplicationModel.chattedCarsMap = new ChattedCarsMap();
        updateCarPickerIcon(0);
        AllChatsActivity.updateToken(FirebaseInstanceId.getInstance().getToken());

        //Clean Forms and Car Views
        CarForm.removeAllForms();
        LicencePlateView.removeAllCarViews();

        //Create Forms And Card Views
        CarForm.createFormsFromCars(ApplicationModel.getCurrentDriver().getCars(),this);
        createCarViewsFromCars();
        AllChatsActivity.addAllMyChattedCarList();

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

    public void setSounds(){
        beepSound = MediaPlayer.create(MainActivity.this, R.raw.beep_sound);
    }

    public static void makeBeepSound(){
        beepSound.start();
    }

    private void initEmojiMap() {

        emojiMap = new HashMap<>();
        emojiMap.put("0",R.drawable.driver1);
        emojiMap.put("1",R.drawable.driver2);
        emojiMap.put("2",R.drawable.driver3);
        emojiMap.put("3",R.drawable.twoboys);
        emojiMap.put("4",R.drawable.twogirls);
        emojiMap.put("5",R.drawable.batmobile);
        emojiMap.put("6",R.drawable.backtothefuture);
        emojiMap.put("7",R.drawable.blondegirl);
        emojiMap.put("8",R.drawable.oldman);
        emojiMap.put("9",R.drawable.taxidriver);
        emojiMap.put("10",R.drawable.taxidriver2);
        emojiMap.put("11",R.drawable.simpsons);
        emojiMap.put("12",R.drawable.blackcar);
        emojiMap.put("13",R.drawable.girlwithbluecar);
        emojiMap.put("14",R.drawable.youngadult);

    }

    @Override
    protected void onResume() {
        //start handler as activity become visible
//
//        handler.postDelayed( runnable = new Runnable() {
//            public void run() {
//                effects.shine(takePhoto,shine);
//                checkIfAllMessagesWereRead();
//                handler.postDelayed(runnable, DELAY);
//
//
//            }
//        }, DELAY);
        checkIfAllMessagesWereRead();
        super.onResume();
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        isActive = true;
    }

    @Override
    public void onStop() {
        super.onStop();
        isActive = false;
        handler.removeCallbacksAndMessages(null);
        Log.d("JERRY", "onStop1: ");
    }

    public static void updateCarPickerIcon(int index) {

        ApplicationModel.setCurrentCar(ApplicationModel.getCurrentDriver().getCars().get(index));

        Log.d("BUBA", "emojie id is : " + ApplicationModel.getCurrentDriver().getCars().get(index).getEmojiId());
        //Set icon
        carPicker.setImageResource(emojiMap.get(ApplicationModel.getCurrentDriver().getCars().get(index).getEmojiId()));

    }

 //   When puting thr super call on note the app doesnt crash and it works fine..
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d("JERRY", "onSaveInstanceState1: ");
        super.onSaveInstanceState(outState);
        Log.d("JERRY", "onSaveInstanceState2: ");


    }

    private void setIds(){

        takePhoto = (ImageView)findViewById(R.id.take_photo);
        carPicker = (ImageView)findViewById(R.id.car_picker);
        addCarBtn = (ImageView) findViewById(R.id.add_car_btn);
        settings = (ImageView)findViewById(R.id.settings);
        shine = (ImageView)findViewById(R.id.shine);
        chatsView = (ImageView)findViewById(R.id.chats);


    }

    public static void checkIfAllMessagesWereRead() {

        Database.findUnreadChatsKeys(new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                chatKeyLastMessageMap = (HashMap<String, Message>) object;

                Log.d("YOGI", "someMessageWereNotRead: " + someMessageWereNotRead);
                if(someMessageWereNotRead){
                    if(chatsView.getDrawable().getConstantState() == context.getResources().getDrawable(R.drawable.inbox_icon).getConstantState()) {
                        makeBeepSound();
                    }
                    chatsView.setImageResource(R.drawable.inbox_icon_unread);
                } else {
                    chatsView.setImageResource(R.drawable.inbox_icon);
                }
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
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
                Log.d("BIBI", "MainActivity : setClickListeners  ");

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openSettings();

            }
        });

        chatsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openAllChats();
            }
        });
    }

    private void openAllChats() {


        Intent intent = new Intent(this,AllChatsActivity.class);
        startActivity(intent);
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
        Log.d("BIBI", "MainActivity : openNewCarDialog1  ");

        dialog.show(getSupportFragmentManager(),"AddNewCarDialog");
        Log.d("BIBI", "MainActivity : openNewCarDialog2  ");

    }


    private void changeCurrentCar(int index) {
        ApplicationModel.setCurrentCar(ApplicationModel.getCurrentDriver().getCars().get(index));
        updateCarPickerIcon(index);
    }


    private void startChatWithAnotherCar() {

        if(chattedCarNumber != null) {
            chattedCarNumber = fieldsChecker.removeAllTokensFromCarNumber(chattedCarNumber);
            //Check if car number is in database, and if it does, open a conversation!
            Database.searchCarByCarNumber(chattedCarNumber, new OnGetDataListener() {
                @Override
                public void onSuccess(Object driver) {
                    if(driver != null) {
                        ApplicationModel.setLastDriverSearch((Driver)driver);
                        openChat(ApplicationModel.getLastCarNumberSearch());
                    }
                    else
                        Toast.makeText(MainActivity.this, "Car was not found in the system...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onStart() {
                    Log.d("CHECK", "Wait for data... ");
                }

                @Override
                public void onFailure() {
                    Toast.makeText(MainActivity.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
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
                    chattedCarNumber = text;
                    return;
                }
            }
            //if you are here it means we iterate all elements in for loop and no car number is valid.
            Toast.makeText(this, "car number was not found. try to take another picture.", Toast.LENGTH_SHORT).show();
        }
    }

    private void openChat(final Car chattedCar) {

        final String messageKey;

        Toast.makeText(this, "chat has been open with " + chattedCar.getCarNumber(), Toast.LENGTH_SHORT).show();

        final Intent intent = new Intent(this,ChatActivity.class);
        intent.putExtra("chattedCar", chattedCar);


        Log.d("LIBI", "openChat:  before start activity1");
        if(ApplicationModel.getCurrentCar().getHashMap() == null ){
            //Current driver dont have a conversation at all
            ApplicationModel.getCurrentCar().setHashMap(new HashMap<String, String>());
            messageKey = ApplicationModel.getCurrentCar().getCarNumber() + chattedCar.getCarNumber();
        } else {
            if(ApplicationModel.getCurrentCar().getHashMap().get(chattedCar.getCarNumber()) != null){
                //Converstaion with chattedCar was allready happenened before
                messageKey = ApplicationModel.getCurrentCar().getHashMap().get(chattedCar.getCarNumber());
            } else {
                //Converstaion with chattedCar was NOT happenened before
                messageKey = ApplicationModel.getCurrentCar().getCarNumber() + chattedCar.getCarNumber();
            }
        }


        Log.d("LIBI", "openChat:  before start activity2");

        ApplicationModel.getCurrentCar().getHashMap().put(chattedCar.getCarNumber(),messageKey);

        int index = ApplicationModel.getLastDriverSearch().getCars().indexOf(chattedCar);
        if(ApplicationModel.getLastDriverSearch().getCars().get(index).getHashMap() == null ){
            ApplicationModel.getLastDriverSearch().getCars().get(index).setHashMap(new HashMap<String, String>());
        }
        ApplicationModel.getLastDriverSearch().getCars().get(index).getHashMap().put(ApplicationModel.getCurrentCar().getCarNumber(),messageKey);


        Log.d("LIBI", "openChat:  before start activity3");
        //update the driver to database with the new chat hash

        Database.findAllMyChattedCar(new OnGetDataListener() {
            @Override
            public void onSuccess(Object object) {

                int addedCarIndex = ApplicationModel.chattedCarsMap.add(chattedCar,ApplicationModel.getCurrentCar(),messageKey);
                Database.saveDriver(ApplicationModel.getCurrentDriver(),FirebaseAuth.getInstance().getCurrentUser().getUid());
                Database.saveDriver(ApplicationModel.getLastDriverSearch(),ApplicationModel.getChattedDriverUid());

                Log.d("LIBI", "openChat:  before start activity4");


                intent.putExtra("index",addedCarIndex);
                startActivity(intent);
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });


    }

    @Override
    public void sendInput(Car car) {

        LinearLayout container = new LinearLayout(this);//fake container;

        Driver driver = ApplicationModel.getCurrentDriver();
        driver.addCar(car);

        //get the new car details and create a card view to that car
        TextView nickname = dynamicallyXML.createTextView(this,car.getNickname(),"sans-serif-smallcaps",40, Color.BLACK, Gravity.CENTER,20,50,10,10);
        LicencePlateView card = new LicencePlateView(nickname, CarForm.allForms.size() - 1 ,container,this,this,car.getCarNumber());
        LicencePlateView.allLicencePlateViews.add(card);
        //Save car to database

        Database.saveDriver(driver,FirebaseAuth.getInstance().getCurrentUser().getUid());
        updateCarPickerIcon(ApplicationModel.getCurrentDriver().getCars().size() -1);

        Log.d("AVATAR", "Current car is : " + ApplicationModel.getCurrentCar().getCarNumber());


    }

    @Override
    public void sendInputToEdit(Car newCar, LicencePlateView licencePlateView, CarForm carForm) {

        //Update data base and update all car views.
        ApplicationModel.getCurrentDriver().getCars().get(licencePlateView.getCardId()).setCarNumber(newCar.getCarNumber());
        ApplicationModel.getCurrentDriver().getCars().get(licencePlateView.getCardId()).setNickname(newCar.getNickname());
        ApplicationModel.getCurrentDriver().getCars().get(licencePlateView.getCardId()).setEmojiId(newCar.getEmojiId());
        Database.saveDriver(ApplicationModel.getCurrentDriver(),FirebaseAuth.getInstance().getCurrentUser().getUid());

        updateCarView(ApplicationModel.getCurrentDriver().getCars().get(licencePlateView.getCardId()), licencePlateView.getCardId());
        if(ApplicationModel.getCurrentCar().getCarNumber().equals(ApplicationModel.getCurrentDriver().getCars().get(licencePlateView.getCardId()).getCarNumber())){
            updateCarPickerIcon(licencePlateView.getCardId());
        }
    }

    private void updateCarView(Car newCar, int index) {

        StringBuilder stringBuilder;

        if(newCar.getNickname().equals(newCar.getCarNumber())){
            if(newCar.getNickname().length() == 7){
                stringBuilder = FieldsChecker.addDashesSevenDigit(newCar.getNickname());
            } else {
                stringBuilder = FieldsChecker.addDashesEightDigit(newCar.getNickname());
            }

        } else{
            stringBuilder = new StringBuilder(newCar.getNickname());
        }

        LicencePlateView.allLicencePlateViews.get(index).getNickname().setText(stringBuilder);
    }

    public void createCarViewsFromCars(){

        StringBuilder carNickname;
        LinearLayout container = new LinearLayout(this); // fake container

        //Set the array of car views
        for(int i = 0; i < ApplicationModel.getCurrentDriver().getCars().size(); i++) {

            Car car = ApplicationModel.getCurrentDriver().getCars().get(i);
            carNickname = new StringBuilder(car.getNickname());
            TextView nickname = dynamicallyXML.createTextView(this, carNickname.toString(),"sans-serif-smallcaps",40, Color.BLACK, Gravity.CENTER, 20, 50, 10, 10);
            final LicencePlateView licencePlateView = new LicencePlateView(nickname, i, container, this, MainActivity.activity, car.getCarNumber());

            CarForm.allForms.get(i).getCarNumberPlaceHolder().setText(car.getCarNumber());
            CarForm.allForms.get(i).getNicknamePlaceHolder().setText(car.getNickname());
            CarForm.allForms.get(i).setEmojiID(car.getEmojiId());

            LicencePlateView.allLicencePlateViews.add(licencePlateView);
        }
    }

    @Override
    public void sendInput(int index) {
        changeCurrentCar(index);
    }


}
