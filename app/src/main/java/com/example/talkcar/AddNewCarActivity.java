package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class AddNewCarActivity extends AppCompatActivity {

    private Database databaseRef;;
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
        databaseRef = new Database();
        newCarForm = new NewCarForm(this,formContainer);

    }

    private void setClickListeners() {

        addNewCarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }

    private void setIds() {

        addNewCarBtn = (Button)findViewById(R.id.add_car_btn);
        formContainer = (LinearLayout)findViewById(R.id.form_container);
    }
}
