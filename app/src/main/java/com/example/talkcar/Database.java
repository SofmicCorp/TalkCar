package com.example.talkcar;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;


public class Database {

    public static DatabaseReference databaseReferenceDrivers =  FirebaseDatabase.getInstance().getReference().child("Drivers");
    public static DatabaseReference databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child("Chats");
    private static long startTime;
    private static long difference;


    public static void searchCarByCarNumber(final String carNumber,final OnGetDataListener listener) {

        startTime = System.currentTimeMillis();

        Log.d("BIBI", "searchCarByCarNumber");

        listener.onStart();
        databaseReferenceDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String uId;
                Driver driver;

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                     driver = postSnapshot.getValue(Driver.class);
                     uId = postSnapshot.getKey();

                    for(int j = 0; j < driver.getCars().size(); j++){
                        if(driver.getCars().get(j).getCarNumber().equals(carNumber)) {

                            ApplicationModel.setLastCarNumberSearch(driver.getCars().get(j));
                            Log.d("BIBI", "Database:searchCarByCarNumber OndataChange" );

                            ApplicationModel.setChattedDriverUid(uId);
                            listener.onSuccess(driver);
                            difference = System.currentTimeMillis() - startTime;
                            Log.d("BUBA", "time that search take : " + difference);
                            return;
                         }
                    }
                }
                //car number was not found.
                listener.onSuccess(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public static void searchDriverByUid(final String uId,final OnGetDataListener listener){

        startTime = System.currentTimeMillis();
        Log.d("BIBI", "start search in database uID...");
        listener.onStart();

        DatabaseReference DriverRef = databaseReferenceDrivers.child(uId);

        // Attach a listener to read the data at our posts reference
        DriverRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Driver driver = dataSnapshot.getValue(Driver.class);
                if(driver != null){
                    Log.d("BIBI", "Databse: OnDataChange1  ");
                    listener.onSuccess(driver);
                    return;
                }
                Log.d("BIBI", "Databse: OnDataChange2 ");

                listener.onSuccess(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                listener.onFailure();
            }
        });
    }

    public static void searchChatByKey(final String key,final OnGetDataListener listener){

        Log.d("BUBA", "key to search is : " + key);
        startTime = System.currentTimeMillis();
        Log.d("BUBA", "start search in database...");
        listener.onStart();


        databaseReferenceChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Chat chat = postSnapshot.getValue(Chat.class);
                    if(chat.getKey().equals(key)) {
                        listener.onSuccess(chat);
                        difference = System.currentTimeMillis() - startTime;
                        Log.d("BUBA", "time that search take : " + difference);
                        return;
                    }
                }

                //If no chat with that key found
                listener.onSuccess(null);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });


    }

    public static void findAllMyChattedCar(final OnGetDataListener listener){

        listener.onStart();

        databaseReferenceDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Car> allMyChattedCar = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    for(int j = 0; j < driver.getCars().size(); j++){
                        for(int k = 0; k < ApplicationModel.getCurrentDriver().getCars().size(); k++){
                            if(ApplicationModel.getCurrentDriver().getCars().get(k).getHashMap() != null) {
                                if (ApplicationModel.getCurrentDriver().getCars().get(k).getHashMap().get(driver.getCars().get(j).getCarNumber()) != null) {
                                    allMyChattedCar.add(driver.getCars().get(j));
                                }
                            }
                        }
                    }
                }
                Log.d("BUBA", "time that search take : " + difference);

                listener.onSuccess(allMyChattedCar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }


    public static void findUnreadChatsKeys(final OnGetDataListener listener){

        listener.onStart();

        databaseReferenceChats.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Message> chatKeyLastMessageMap = new HashMap<>(); //<Chat Key,Last Message>

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    for (int i = 0; i < ApplicationModel.getCurrentDriver().getCars().size() ; i++) {
                        if(ApplicationModel.getCurrentDriver().getCars().get(i).getHashMap() != null){
                        for (int j = 0; j < ApplicationModel.getCurrentDriver().getCars().get(i).getHashMap().size(); j++) {
                            if (postSnapshot.getKey().equals(ApplicationModel.getCurrentDriver().getCars().get(i).getHashMap().values().toArray()[j])) {
                                if(postSnapshot.getValue(Chat.class).isSomeMessageWereNotRead()){
                                    int lastMessageIndex = postSnapshot.getValue(Chat.class).getMessages().size() - 1;
                                    Message lastMessage = postSnapshot.getValue(Chat.class).getMessages().get(lastMessageIndex);
                                    if(lastMessage.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        MainActivity.someMessageWereNotRead = true;
                                        chatKeyLastMessageMap.put(postSnapshot.getValue(Chat.class).getKey(),lastMessage);
                                    }
                                }
                            }
                        }
                        }
                    }
                }

                Log.d("BUBA", "time that search take : " + difference);

                listener.onSuccess(chatKeyLastMessageMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });

    }

    public static void saveDriver(final Driver driver, final String uId) {

        Log.d("BIBI", "Database: saveDriver: ");
        databaseReferenceDrivers.child(uId).setValue(driver);
    }

    public static void saveChat(Chat chat){

        Log.d("SIMBA", "chat : " + chat);
        Log.d("SIMBA", "chat.GetKey : " + chat.getKey());

        databaseReferenceChats.child(chat.getKey()).setValue(chat);
    }

    public static void deleteCar(String index){
        databaseReferenceDrivers.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(index).removeValue();
    }
}

