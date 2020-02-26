package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.talkcar.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

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
        //updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    private void addAllMyChattedCarList() {


        database.findAllMyChattedCar(new OnGetDataListener() {
            @Override
            public void onSuccess(Object chattedCarList) {

                if(chattedCarList != null){
                    //There is at least one conversation
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

    public static void updateToken(String token){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }
}
