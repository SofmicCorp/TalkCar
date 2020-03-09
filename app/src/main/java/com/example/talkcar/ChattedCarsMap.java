package com.example.talkcar;

import java.util.ArrayList;

public class ChattedCarsMap {
    private ArrayList<Car> chattedCars;
    private ArrayList<String> keyChats;

    public ChattedCarsMap() {
        this.chattedCars = new ArrayList<>();
        this.keyChats = new ArrayList<>();
    }

    public ArrayList<Car> getChattedCars() {
        return chattedCars;
    }

    public ArrayList<String> getKeyChats() {
        return keyChats;
    }

    public int add(Car car, String key){

        for (int i = 0; i < keyChats.size(); i++){
            if(keyChats.get(i).equals(key)){
                return i;
            }
        }
        chattedCars.add(car);
        keyChats.add(key);
        return lastIndex();
    }

    public void remove(Car car){

        for (int i = 0; i < chattedCars.size(); i++) {
            if (car.equals(chattedCars.get(i))){
                chattedCars.remove(i);
                keyChats.remove(i);
                return;
            }
        }
    }

    public void reset(){
        chattedCars.clear();
        keyChats.clear();
    }

    public int lastIndex(){

        return chattedCars.size() - 1;
    }
}
