package com.example.talkcar;


import android.util.Log;

import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class Database {

    public DatabaseReference databaseReference;
    private Hashable hashRef;
    private static long startTime;
    private static long difference;


    public Database(Hashable hashRef){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Drivers");
        this.hashRef = hashRef;
    }

    public void searchCarByCarNumber(final String carNumber,final OnGetDataListener listener) {

        startTime = System.currentTimeMillis();

        Log.d("BUBA", "start search in database...");
        listener.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Could be a lot more efficent! not iterate through all data base but to look spicfivly\
                //car with that car number "text" have to be fixed!

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    for(int j = 0; j < driver.getCars().size(); j++){
                        if(driver.getCars().get(j).getCarNumber().equals(carNumber)) {
                            ApplicationModel.setLastCarNumberSearch(carNumber);
                            listener.onSuccess(driver);

                            difference = System.currentTimeMillis() - startTime;
                            Log.d("BUBA", "time that search take : " + difference);
                            return;
                         }
                    }
                }

                //car number was not found.
                ApplicationModel.setLastCarNumberSearch(null);
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

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Could be a lot more efficent! not iterate through all data base but to look spicfivly\
                //car with that car number "text" have to be fixed!
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    if(driver.getEmail().equals(email)) {
                        ApplicationModel.setCurrentDriver(driver);
                        listener.onSuccess(driver);
                        difference = System.currentTimeMillis() - startTime;
                        Log.d("BUBA", "time that search take : " + difference);
                        return;
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

        databaseReference.child(hashRef.hash(driver.getEmail().toString())).setValue(driver);
    }

}
