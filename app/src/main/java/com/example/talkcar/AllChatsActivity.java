package com.example.talkcar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.talkcar.Notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllChatsActivity extends AppCompatActivity {

    public static boolean isActive;
    private static RecyclerView recyclerView;
    public static CarAdapter carAdapter;
    private static LinearLayout messageContainer;
    private static TextView noMessageToShowTV;
    private List<Car> carList;
    public static Context context;
    public static DynamicallyXML dynamicallyXML = new DynamicallyXML();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_chats);
        Log.d("LUBA", "onCreate: ");
        Log.d("LUBA", "Recyevler View before setId: " + recyclerView);
        setIds();
        Log.d("LUBA", "Recyevler View after setId: " + recyclerView);
        context = this;
        addAllMyChattedCarList();

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

        Log.d("LUBA", "onStop!: ");
        carAdapter = null;
        isActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        handler.postDelayed( runnable = new Runnable() {
//            public void run() {
//                addAllMyChattedCarList();
//                    handler.postDelayed(runnable, DELAY);
//            }
//        }, DELAY);

        addAllMyChattedCarList();
    }

    public static void addAllMyChattedCarList() {

        Database.findAllMyChattedCar(new OnGetDataListener() {
            @Override
            public void onSuccess(final Object chattedCarMap) {

                Database.findUnreadChatsKeys(new OnGetDataListener() {
                    @Override
                    public void onSuccess(Object chatKeyLastMessageMap) {

                        if(chattedCarMap != null){
                            //There is at least one conversation
                            if(AllChatsActivity.isActive) {
                                readCars((ChattedCarsMap) chattedCarMap, (HashMap<String, Message>) chatKeyLastMessageMap);
                            }
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

    public static void readCars(ChattedCarsMap chattedCarsMap, HashMap<String, Message> chatKeyLastMessageMap) {

        Log.d("LUBA", "recycler view in readCars method: "+ recyclerView);
        Log.d("LUBA", "carAdapter view in readCars method: "+ carAdapter);

        if(noMessageToShowTV != null){
            messageContainer.removeView(noMessageToShowTV);
        }

        if(chattedCarsMap.getChattedCars().size() == 0 && AllChatsActivity.isActive){
            noMessageToShowTV = dynamicallyXML.createTextView(context,"We didn't find anything to show here","sans-serif-condensed",15, Color.BLACK, Gravity.CENTER,0,100,0,0);
            messageContainer.addView(noMessageToShowTV);
        }  else {
            //There at least one conversation
            if (carAdapter == null) {
                carAdapter = new CarAdapter(context, chattedCarsMap, chatKeyLastMessageMap);
                if (recyclerView != null) {
                    recyclerView.setAdapter(carAdapter);
                }
                return;
            }

            if (chatKeyLastMessageMap.size() > 0) {
                carAdapter = new CarAdapter(context, chattedCarsMap, chatKeyLastMessageMap);
                recyclerView.setAdapter(carAdapter);
            } else {
                Log.d("KUBA", "carAdapter from allLChats: " + carAdapter.getHolder());
                Log.d("KUBA", "HOLDER from allLChats: " + carAdapter.getHolder());
                if (carAdapter.getHolder() != null) {
                    carAdapter.getHolder().chattedProfileImageBackground.setImageResource(R.drawable.whitecircle);
                }
            }
        }
    }

    private void setIds() {

        messageContainer = findViewById(R.id.messages_container);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(AllChatsActivity.this));

        if(carList == null) {
            carList = new ArrayList<>();
        }
    }

    public static void updateToken(String token){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public static void deleteAllChatOfCar(Car carToDelete) {

        if(carToDelete.getHashMap() != null) {
            for (String keyChat : carToDelete.getHashMap().values()) {
                Database.deleteChat(keyChat);
            }
        }
    }
}
