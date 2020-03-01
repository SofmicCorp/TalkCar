package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.talkcar.Notifications.Data;
import com.example.talkcar.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class AllChatsActivity extends AppCompatActivity {

    public static boolean isActive;
    private static RecyclerView recyclerView;
    public static CarAdapter carAdapter;
    private List<Car> carList;
    private Handler handler;
    private Runnable runnable;
    private final int DELAY = 3*1000; //Delay for 3 seconds.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);
        setIds();
        handler = new Handler();
        //updateToken(FirebaseInstanceId.getInstance().getToken());
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.postDelayed( runnable = new Runnable() {
            public void run() {
                addAllMyChattedCarList();
                handler.postDelayed(runnable, DELAY);

            }
        }, DELAY);

        addAllMyChattedCarList();
    }

    private void addAllMyChattedCarList() {

        Database.findAllMyChattedCar(new OnGetDataListener() {
            @Override
            public void onSuccess(final Object chattedCarList) {

                Database.findUnreadChatsKeys(new OnGetDataListener() {
                    @Override
                    public void onSuccess(Object chatKeyLastMessageMap) {

                        if(chattedCarList != null){
                            //There is at least one conversation
                            readCars((ArrayList<Car>)chattedCarList,(HashMap<String,Message>)chatKeyLastMessageMap);
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

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void readCars(ArrayList<Car> chattedCarList, HashMap<String, Message> chatKeyLastMessageMap) {

//        if(carAdapter == null){
//            carAdapter = new CarAdapter(AllChatsActivity.this, chattedCarList, chatKeyLastMessageMap);
//            recyclerView.setAdapter(carAdapter);
//            return;
//        }
//
//        if(chatKeyLastMessageMap.size() > 0) {
//            carAdapter = new CarAdapter(AllChatsActivity.this, chattedCarList, chatKeyLastMessageMap);
//            recyclerView.setAdapter(carAdapter);
//        } else {
//            Log.d("LUBA", "here!!: ");
//            carAdapter.getHolder().profileImageBackground.setImageResource(R.drawable.whitecircle);
//            recyclerView.setAdapter(carAdapter);
//        }

        carAdapter = new CarAdapter(AllChatsActivity.this, chattedCarList, chatKeyLastMessageMap);
        recyclerView.setAdapter(carAdapter);

    }

    private void setIds() {

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllChatsActivity.this));
        carList = new ArrayList<>();
    }

    public static void updateToken(String token){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }
}
