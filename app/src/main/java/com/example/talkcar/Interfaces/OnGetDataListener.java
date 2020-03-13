package com.example.talkcar.Interfaces;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListener {
    //this is for callbacks
    void onSuccess(Object object);
    void onStart();
    void onFailure();
}
