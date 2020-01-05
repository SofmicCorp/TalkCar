package com.example.talkcar;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CarNumberChecker {

    DatabaseReference databaseReference;

    private final int ZERO = 48;
    private final int NINE = 57;
    private final int DASH = 45;
    private final int SPACE = 32;
    private final int DOTS = 58;
    private final int CAR_NUMBER_LENGTH = 7;


    public boolean isValidCarNumber(String text) {

        if(text.length() < CAR_NUMBER_LENGTH)
            return false;

        for(int i = 0; i < text.length(); i++){
            if(((text.charAt(i) < ZERO && text.charAt(i) != DASH) &&  (text.charAt(i) < ZERO && text.charAt(i) != SPACE)) ||
                    (text.charAt(i) > NINE && text.charAt(i) != DOTS )) {
                return false;
            }
        }
        return true;
    }

    public String getCarNumberFromDatabase(final String text) {

        final String[] carNumber = new String[1];

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Driver");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                     Driver driver = postSnapshot.getValue(Driver.class);
                     if(driver.getCarNumber().equals(text))
                         carNumber[0] = text;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return carNumber[0];
    }

    public String removeAllTokensFromCarNumber(String text) {

        //very inefficient
        String newCarNumber = text.replaceAll(":" ,"");
        newCarNumber = newCarNumber.replaceAll("\\s","");
        newCarNumber = newCarNumber.replaceAll("-","");

        return newCarNumber;
    }
}
