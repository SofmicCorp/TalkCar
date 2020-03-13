package com.example.talkcar.Helpers;

import android.widget.EditText;

import com.example.talkcar.Database.Database;

public class FieldsChecker {

    private final int ZERO = 48;
    private final int NINE = 57;
    private final int DASH = 45;
    private final int SPACE = 32;
    private final int DOTS = 58;
    private final int MIN_CAR_NUMBER_LENGTH = 7;
    private final int MAX_CAR_NUMBER_LENGTH = 8;
    private final int MAX_NICKNAME_LENGTH = 10;
    private Database database;
    private static Boolean isExits;

    public boolean isValidCarNumber(String text) {

        if(text.length() < MIN_CAR_NUMBER_LENGTH)
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

    public boolean checkCarDetailsFields(final EditText carNumberPlaceHolder, EditText nicknamePlaceHolder) {

        if(carNumberPlaceHolder.getText().toString().isEmpty()) {
            carNumberPlaceHolder.setError("Please enter car number");
            return false;
        }

        if(carNumberPlaceHolder.getText().toString().length() <  MIN_CAR_NUMBER_LENGTH ||
                carNumberPlaceHolder.getText().toString().length() >  MAX_CAR_NUMBER_LENGTH || !carNumberPlaceHolder.getText().toString().matches("[0-9]+")){
            carNumberPlaceHolder.setError("Illegal car number");
            return false;
        }

        if(nicknamePlaceHolder.getText().toString().isEmpty()){
            //Nickname becomes car number
            nicknamePlaceHolder.setText(carNumberPlaceHolder.getText());
        }

        if(nicknamePlaceHolder.getText().toString().length() > MAX_NICKNAME_LENGTH){
            //Nickname becomes car number
            nicknamePlaceHolder.setText(nicknamePlaceHolder.getText().toString().substring(0,MAX_NICKNAME_LENGTH));
        }


        return true;
    }





    public boolean checkUserDetailsFields(EditText namePlaceHolder, EditText emailPlaceHolder, EditText passwordPlaceHolder){

        String name = namePlaceHolder.getText().toString().trim();
        String email = emailPlaceHolder.getText().toString().trim();
        String pwd = passwordPlaceHolder.getText().toString().trim();


        if(name.isEmpty()){
            namePlaceHolder.setError("Please enter your name");
            return false;

        }
         else if(!checkLoginFields(emailPlaceHolder,passwordPlaceHolder)){
            return false;
        }

        if(name.length() > MAX_NICKNAME_LENGTH){
            namePlaceHolder.setText(name.substring(0, MAX_NICKNAME_LENGTH));
        }

        return true;

    }

    public boolean checkLoginFields(EditText emailPlaceHolder, EditText passwordPlaceHolder){

        String email = emailPlaceHolder.getText().toString().trim();
        String pwd = passwordPlaceHolder.getText().toString().trim();

         if(email.isEmpty()){
            emailPlaceHolder.setError("Please enter email");
            emailPlaceHolder.requestFocus();
            return false;

        }else if(pwd.isEmpty() ){
            passwordPlaceHolder.setError("Please enter password");
            return false;
        }

         return true;
    }



    public static StringBuilder addDashesSevenDigit(String carNumber) {

        StringBuilder carNumberWithDashes = new StringBuilder();

        for(int i = 0; i < carNumber.length(); i++){

            if(i ==  2 || i == 5){
                carNumberWithDashes.append('-');
            }
            carNumberWithDashes.append(carNumber.charAt(i));

        }


        return carNumberWithDashes;
    }

    public static StringBuilder addDashesEightDigit(String carNumber) {

        StringBuilder carNumberWithDashes = new StringBuilder();

        for(int i = 0; i < carNumber.length(); i++){

            if(i ==  3 || i == 5){
                carNumberWithDashes.append('-');
            }
            carNumberWithDashes.append(carNumber.charAt(i));

        }


        return carNumberWithDashes;
    }
}
