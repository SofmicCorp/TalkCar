package com.example.talkcar;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Database {

    private DatabaseReference databaseReference;


    public Database(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Drivers");

    }

    public void updateLastCarNumberSearch(final String text,final OnGetDataListener listener) {

        listener.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Could be a lot more efficent! not iterate through all data base but to look spicfivly\
                //car with that car number "text" have to be fixed!

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);

                    for(int j = 0; j < driver.getCars().size(); j++){
                        if(driver.getCarNumber(j).equals(text)) {
                            LoginActivity.applicationModel.setLastCarNumberSearch(text);
                            listener.onSuccess(dataSnapshot);
                            return;
                         }
                    }
                }

                LoginActivity.applicationModel.setLastCarNumberSearch(null);
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void updateCurrentDriverByEmail(final String email,final OnGetDataListener listener){

        listener.onStart();

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Could be a lot more efficent! not iterate through all data base but to look spicfivly\
                //car with that car number "text" have to be fixed!
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    for(int j = 0; j < driver.getCars().size(); j++){
                        if(driver.getEmail().equals(email)) {
                            LoginActivity.applicationModel.setCurrentDriver(driver);
                            listener.onSuccess(dataSnapshot);
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });
    }

    public void saveDriver(Driver driver) {

        databaseReference.child(MD5_Hash(driver.getEmail().toString())).setValue(driver);

    }

    public void updateDriver(Driver driver){

    }

    //When using this functio, the key to every driver in firebase can be the hashed email make the key uniqe.
    //We are doing this because key can not contain @ / . / etc...
    public String MD5_Hash(String s) {

        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
