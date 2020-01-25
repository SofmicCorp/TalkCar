package com.example.talkcar;

import com.google.firebase.database.DataSnapshot;

public interface OnGetDataListener {
    //this is for callbacks
    void onSuccess(Driver driver);
    void onStart();
    void onFailure();
}
