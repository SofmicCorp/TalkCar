package com.example.talkcar;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Database {

    private DatabaseReference databaseReference;
    private String lastCarNumberSearch;
    private Driver currentDriver;

    public Database(){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Driver");
    }

    public void getCarNumber(final String text) {

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Could be a lot more efficent! not iterate through all data base but to look spicfivly\
                //car with that car number "text" have to be fixed!

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    if(driver.getCarNumber().equals(text)) {
                        lastCarNumberSearch = text;
                        Log.d("GUGU", "LastCarSearch = " + lastCarNumberSearch);
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getDriverByCarNumber(final String text){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Could be a lot more efficent! not iterate through all data base but to look spicfivly\
                //car with that car number "text" have to be fixed!

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    if(driver.getCarNumber().equals(text)) {
                        currentDriver = driver;
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void saveDriver(Driver driver) {

        databaseReference.push().setValue(driver);
    }

    public String getLastCarNumberSearch() {
        return lastCarNumberSearch;
    }

    public Driver getCurrentDriver() {
        return currentDriver;
    }
}
