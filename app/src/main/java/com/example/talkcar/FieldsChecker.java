package com.example.talkcar;

import android.widget.EditText;
import android.widget.Toast;

public class FieldsChecker {

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


    public String removeAllTokensFromCarNumber(String text) {

        //very inefficient
        String newCarNumber = text.replaceAll(":" ,"");
        newCarNumber = newCarNumber.replaceAll("\\s","");
        newCarNumber = newCarNumber.replaceAll("-","");

        return newCarNumber;
    }

    public boolean checkCarDetailsFields(EditText carNumberPlaceHolder, EditText nicknamePlaceHolder) {

            if(carNumberPlaceHolder.getText().toString().isEmpty()) {
                carNumberPlaceHolder.setError("Please enter car number");
                return false;
            }

            if(nicknamePlaceHolder.getText().toString().isEmpty()){
                nicknamePlaceHolder.setText(carNumberPlaceHolder.getText());
            }
        return true;
    }


    public boolean checkUserDetailsFields(EditText emailPlaceHolder, EditText passwordPlaceHolder){

        String email = emailPlaceHolder.getText().toString().trim();
        String pwd = passwordPlaceHolder.getText().toString().trim();

        if(email.isEmpty()){
            emailPlaceHolder.setError("Please enter email");
            emailPlaceHolder.requestFocus();

            return false;

        }else if(pwd.isEmpty()){
            passwordPlaceHolder.setError("Please enter your password");

            return false;
        }

        return true;

    }
}
