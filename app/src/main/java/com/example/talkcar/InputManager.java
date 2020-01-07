package com.example.talkcar;

import android.widget.EditText;

import java.util.ArrayList;

public class InputManager {

    private ArrayList<EditText> allCarNumbers;
    private ArrayList<EditText> allNickNames;
    private ArrayList<String> allEmojisIds;

    public InputManager(){

        allCarNumbers = new ArrayList<>();
        allNickNames = new ArrayList<>();
        allEmojisIds = new ArrayList<>();
    }

    public ArrayList<EditText> getAllCarNumbers() {
        return allCarNumbers;
    }

    public ArrayList<EditText> getAllNickNames() {
        return allNickNames;
    }

    public ArrayList<String> getAllEmojisIds() {
        return allEmojisIds;
    }
}
