package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class AllChatsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CarAdapter carAdapter;
    private List<Car> carList;
    private Database database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);
        setIds();
        addAllMyChattedCarList();

    }

    private void addAllMyChattedCarList() {


        database.findAllMyChattedCar(new OnGetDataListener() {
            @Override
            public void onSuccess(Object chattedCarList) {

                if(chattedCarList != null){
                    //list is not empty
                    readCars((ArrayList<Car>)chattedCarList);
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

    private void readCars(ArrayList<Car> chattedCarList) {

        carAdapter = new CarAdapter(AllChatsActivity.this,chattedCarList);
        recyclerView.setAdapter(carAdapter);


    }

    private void setIds() {

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllChatsActivity.this));
        carList = new ArrayList<>();
        database= new Database(new MD5());
    }
}
