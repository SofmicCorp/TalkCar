package com.example.talkcar.Chats;

import com.example.talkcar.Cars.Car;

import java.util.ArrayList;

public class ChattedCarsMap {
    private ArrayList<Car> chattedCars;
    private ArrayList<Car> myCars;
    private ArrayList<String> keyChats;


    public ChattedCarsMap() {
        this.chattedCars = new ArrayList<>();
        this.myCars = new ArrayList<>();
        this.keyChats = new ArrayList<>();
    }

    public ArrayList<Car> getChattedCars() {
        return chattedCars;
    }

    public ArrayList<String> getKeyChats() {
        return keyChats;
    }

    public ArrayList<Car> getMyCars() {
        return myCars;
    }

    public int add(Car chattedCar,Car myCar, String key){

        for (int i = 0; i < keyChats.size(); i++){
            if(keyChats.get(i).equals(key)){
                return i;
            }
        }
        chattedCars.add(chattedCar);
        myCars.add(myCar);
        keyChats.add(key);
        return lastIndex();
    }

    public void remove(Car chattedCar){

        for (int i = 0; i < chattedCars.size(); i++) {
            if (chattedCar.equals(chattedCars.get(i))){
                chattedCars.remove(i);
                myCars.remove(i);
                keyChats.remove(i);
                return;
            }
        }
    }

    public void reset(){
        chattedCars.clear();
        myCars.clear();
        keyChats.clear();
    }

    public int lastIndex(){

        return chattedCars.size() - 1;
    }
}
