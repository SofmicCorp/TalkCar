package com.example.talkcar;


import androidx.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class Database {

    private DatabaseReference databaseReference;
    private Hashable hashRef;


    public Database(Hashable hashRef){

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Drivers");
        this.hashRef = hashRef;
    }

    public void updateLastCarNumberSearch(final String carNumber,final OnGetDataListener listener) {

        listener.onStart();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Could be a lot more efficent! not iterate through all data base but to look spicfivly\
                //car with that car number "text" have to be fixed!

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Driver driver = postSnapshot.getValue(Driver.class);
                    for(int j = 0; j < driver.getCars().size(); j++){
                        if(driver.getCars().get(j).equals(carNumber)) {
                            LoginActivity.applicationModel.setLastCarNumberSearch(carNumber);
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
                    if(driver.getEmail().equals(email)) {
                        LoginActivity.applicationModel.setCurrentDriver(driver);
                        listener.onSuccess(dataSnapshot);
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

    public void updateDriver(Driver driver){

    }
}
