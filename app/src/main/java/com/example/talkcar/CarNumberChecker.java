package com.example.talkcar;

import android.util.Log;

public class CarNumberChecker {

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
}
