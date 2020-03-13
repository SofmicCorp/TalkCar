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

        //The function return the driver and save the car at the Application Model

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

    public static void findChatByKey(String chatKey,final OnGetDataListener listener){

        listener.onStart();

        databaseReferenceChats.child(chatKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot != null){
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    listener.onSuccess(chat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public static void findAllMyChattedCar(final OnGetDataListener listener){

        listener.onStart();

        databaseReferenceDrivers.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                ApplicationModel.chattedCarsMap.reset();
                Log.d("KUCHINI", "im here1!:");

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Log.d("KUCHINI", "im here2!:");

                    Driver driver = postSnapshot.getValue(Driver.class);
                    for(int j = 0; j < driver.getCars().size(); j++){
                        if(driver.getuId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            break;
                        if(ApplicationModel.getCurrentDriver() != null) {
                            Log.d("KUCHINI", "im here3!:");
                            Log.d("KUCHINI", "SIZE OF CARS: " + ApplicationModel.getCurrentDriver().getCars().size());
                            for (int k = 0; k < ApplicationModel.getCurrentDriver().getCars().size(); k++) {
                                Log.d("KUCHINI", "im here4!:");
                                if (ApplicationModel.getCurrentDriver().getCars().get(k).getHashMap() != null) {
                                    Log.d("KUCHINI", "im here5!:");
                                    Log.d("KUCHINI", " ApplicationModel.getCurrentDriver().getCars().get(k).getHashMap()" +  ApplicationModel.getCurrentDriver().getCars().get(k).getHashMap());
                                    String chatKey = ApplicationModel.getCurrentDriver().getCars().get(k).getHashMap().get(driver.getCars().get(j).getCarNumber());
                                    if (chatKey!= null) {
                                        Log.d("KUCHINI", "im here6!:");
                                        Log.d("KUCHINI", "driver.getCars().get(j):" + driver.getCars().get(j));
                                        ApplicationModel.chattedCarsMap.add(driver.getCars().get(j),chatKey);
                                    }
                                }
                            }
                        }
                    }
                }
                Log.d("BUBA", "time that search take : " + difference);

                listener.onSuccess(ApplicationModel.chattedCarsMap);
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
                    if(ApplicationModel.getCurrentDriver() != null){
                    for (int i = 0; i < ApplicationModel.getCurrentDriver().getCars().size() ; i++) {
                        if (ApplicationModel.getCurrentDriver().getCars().get(i).getHashMap() != null) {
                            for (int j = 0; j < ApplicationModel.getCurrentDriver().getCars().get(i).getHashMap().size(); j++) {
                                if (postSnapshot.getKey().equals(ApplicationModel.getCurrentDriver().getCars().get(i).getHashMap().values().toArray()[j])) {
                                    if (postSnapshot.getValue(Chat.class).isSomeMessageWereNotRead()) {
                                        int lastMessageIndex = postSnapshot.getValue(Chat.class).getMessages().size() - 1;
                                        Message lastMessage = postSnapshot.getValue(Chat.class).getMessages().get(lastMessageIndex);
                                        if (lastMessage.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                            MainActivity.someMessageWereNotRead = true;
                                            chatKeyLastMessageMap.put(postSnapshot.getValue(Chat.class).getKey(), lastMessage);
                                        }
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

    public static void findAllLastMessagesToSpecificDriver(final String uId,final OnGetDataListener listener){

        listener.onStart();
        final ArrayList<Message> allLastMessages = new ArrayList<>();

        databaseReferenceChats.addValueEventListener(new ValueEventListener(){

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Chat chat = postSnapshot.getValue(Chat.class);
                    int size = chat.getMessages().size() - 1;
                    if(chat.getMessages() != null && (chat.getMessages().get(size).getSender().equals(uId) ||chat.getMessages().get(size).getReceiver().equals(uId))) {
                        allLastMessages.add(chat.getMessages().get(size));
                    }
                }
                listener.onSuccess(allLastMessages);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public static void saveDriver(final Driver driver, final String uId) {

        Log.d("BIBI", "Database: saveDriver: ");
        //ApplicationModel.setCurrentDriver(driver);
        databaseReferenceDrivers.child(uId).setValue(driver);
    }

    public static void saveChat(Chat chat){

        Log.d("SIMBA", "chat : " + chat);
        Log.d("SIMBA", "chat.GetKey : " + chat.getKey());

        databaseReferenceChats.child(chat.getKey()).setValue(chat);
    }


    public static void deleteChat(String keyChat) {

        databaseReferenceChats.child(keyChat).removeValue();
    }
}

