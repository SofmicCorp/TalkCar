package com.example.talkcar;


import android.util.Log;

import androidx.annotation.NonNull;
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

    public DatabaseReference databaseReferenceDrivers;
    public DatabaseReference databaseReferenceChats;
    private Hashable hashRef;
    private static long startTime;
    private static long difference;


    public Database(Hashable hashRef){

        databaseReferenceDrivers = FirebaseDatabase.getInstance().getReference().child("Drivers");
        databaseReferenceChats = FirebaseDatabase.getInstance().getReference().child("Chats");
        this.hashRef = hashRef;
    }

    public void searchCarByCarNumber(final String carNumber,final OnGetDataListener listener) {

        startTime = System.currentTimeMillis();

        Log.d("BUBA", "start search in database...");
        listener.onStart();
        databaseReferenceDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    for(int j = 0; j < driver.getCars().size(); j++){
                        if(driver.getCars().get(j).getCarNumber().equals(carNumber)) {
                            ApplicationModel.setLastCarNumberSearch(driver.getCars().get(j));
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

    public void searchDriverByEmail(final String email,final OnGetDataListener listener){

        startTime = System.currentTimeMillis();
        Log.d("BUBA", "start search in database...");
        listener.onStart();

        databaseReferenceDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    if(driver.getEmail().equals(email)) {
                        Log.d("BUBA", "driver is from onDataChange : " + driver);
                        listener.onSuccess(driver);
                        difference = System.currentTimeMillis() - startTime;
                        Log.d("BUBA", "time that search take : " + difference);
                        return;
                    }
                }

                listener.onSuccess(null);
                //If no driver with that email found
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void searchChatByKey(final String key,final OnGetDataListener listener){

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

    public void findAllMyChattedCar(final OnGetDataListener listener){

        listener.onStart();

        databaseReferenceDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ArrayList<Car> allMyChattedCar = new ArrayList<>();

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    for(int j = 0; j < driver.getCars().size(); j++){
                        for(int k = 0; k < ApplicationModel.getCurrentDriver().getCars().size(); k++){
                            if(ApplicationModel.getCurrentDriver().getCars().get(k).getHashMap().get(driver.getCars().get(j).getCarNumber()) != null){
                                allMyChattedCar.add(driver.getCars().get(j));
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

    public void saveDriver(Driver driver) {

        databaseReferenceDrivers.child(hashRef.hash(driver.getEmail().toString())).setValue(driver);
    }

    public void saveChat(Chat chat){

        databaseReferenceChats.child(chat.getKey()).setValue(chat);
    }

    public void deleteCar(String index){
        databaseReferenceDrivers.child(hashRef.hash(ApplicationModel.getCurrentDriver().getEmail().toString())).child(index).removeValue();
    }

}
