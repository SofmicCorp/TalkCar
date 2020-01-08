package com.example.talkcar;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {

    private DatabaseReference databaseReference;


    public Database(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Driver");
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

        databaseReference.push().setValue(driver);
    }

}
