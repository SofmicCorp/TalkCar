package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class AddNewCarActivity extends AppCompatActivity {

    private Database databaseRef;
    private Button addNewCarBtn;
    private NewCarForm newCarForm;
    private LinearLayout formContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_car);
        NewCarForm.removeAllForms();
        setIds();
        setClickListeners();
        databaseRef = new Database(new MD5());
        newCarForm = new NewCarForm(this,formContainer);

    }

    private void setClickListeners() {

        addNewCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               createNewCar();
            }
        });
    }

    private void setIds() {

        addNewCarBtn = (Button)findViewById(R.id.add_car_btn);
        formContainer = (LinearLayout)findViewById(R.id.form_container);
    }

    private void createNewCar(){

        String carNumber = newCarForm.getCarNumberPlaceHolder().getText().toString();
        String nickName = newCarForm.getNicknamePlaceHolder().getText().toString();
        String emojiId = newCarForm.getEmojiID();

        if(carNumber.isEmpty()){
            newCarForm.getCarNumberPlaceHolder().setError("Please enter car number");
            return;
        }

        if(nickName.isEmpty()){
            nickName = carNumber;
        }

        if(isCarNumberAlreadyExists()){


        }

        Car car = new Car(carNumber,nickName,emojiId);
        Driver driver = LoginActivity.applicationModel.getCurrentDriver();
        driver.addCar(car);

        //Save car to database
        databaseRef.saveDriver(LoginActivity.applicationModel.getCurrentDriver());
        Intent intent = new Intent(AddNewCarActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private boolean isCarNumberAlreadyExists() {

        //need to be completed. need to check is car number is not exists in data base!
        return false;

    }
}
