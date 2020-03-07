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
import java.util.Map;
import java.util.Set;

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
        Log.d("LUBA", "onCreate: ");
        Log.d("LUBA", "Recyevler View before setId: " + recyclerView);
        setIds();
        Log.d("LUBA", "Recyevler View after setId: " + recyclerView);
        handler = new Handler();
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
        handler.removeCallbacksAndMessages(null);
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

            findAllMyChattedCar();

                Database.findUnreadChatsKeys(new OnGetDataListener() {
                    @Override
                    public void onSuccess(Object chatKeyLastMessageMap) {

                        Log.d("SUBA", ":ApplicationModel.allChattedCars =  " + ApplicationModel.allChattedCars);

                        if(ApplicationModel.allChattedCarArray != null){
                            //There is at least one conversation
                            readCars(ApplicationModel.allChattedCars,ApplicationModel.allChattedCarArray,(HashMap<String,Message>)chatKeyLastMessageMap);
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

    private void readCars(HashMap<Car, String> allChattedCarMap, ArrayList<Car> chattedCarList, HashMap<String, Message> chatKeyLastMessageMap) {

        Log.d("LUBA", "recycler view in readCars method: "+ recyclerView);
        Log.d("LUBA", "carAdapter view in readCars method: "+ carAdapter);

        if(carAdapter == null){
            carAdapter = new CarAdapter(AllChatsActivity.this,allChattedCarMap, chattedCarList, chatKeyLastMessageMap);
            if(recyclerView != null) {
                Log.d("LUBA", "here!!!!!!!!!!: ");
                recyclerView.setAdapter(carAdapter);
            }
            return;
        }

        if(chatKeyLastMessageMap.size() > 0) {
                carAdapter = new CarAdapter(AllChatsActivity.this, allChattedCarMap,chattedCarList, chatKeyLastMessageMap);
                recyclerView.setAdapter(carAdapter);
        }

           else {
            Log.d("KUBA", "carAdapter from allLChats: " + carAdapter.getHolder());
            Log.d("KUBA", "HOLDER from allLChats: " + carAdapter.getHolder());
               if(carAdapter.getHolder() != null) {
                   carAdapter.getHolder().profileImageBackground.setImageResource(R.drawable.whitecircle);
               }
        }
    }

    private void setIds() {

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

    public static void findAllMyChattedCar(){

        Database.searchDriverByUid(FirebaseAuth.getInstance().getCurrentUser().getUid(), new OnGetDataListener() {
            @Override
            public void onSuccess(Object driver) {

                if(driver == null)
                    return;

                Driver dbDriver = (Driver)driver;
                HashMap<Car,String> allMyCahttedCarMap = new HashMap<>(); //<Chatted
                ArrayList<Car> allMyChattedCar = new ArrayList<>();

                for (Car car : dbDriver.getCars()) {
                    if(car.getHashMap() != null) {
                        for (Map.Entry<String, Car> entry : car.getHashMap().entrySet()) {
                            allMyCahttedCarMap.put(entry.getValue(), entry.getKey());
                            allMyChattedCar.add(entry.getValue());
                        }
                    }
                }

                ApplicationModel.allChattedCars = allMyCahttedCarMap;
                ApplicationModel.allChattedCarArray = allMyChattedCar;
            }

            @Override
            public void onStart() {

            }

            @Override
            public void onFailure() {

            }
        });


    }
}
