package com.example.talkcar;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Driver implements Serializable {

    private String name;
    private ArrayList<Car> cars;

    public Driver(){

    }

    public Driver(String name,String email){
        this.name = name;
        cars = new ArrayList<>();
    }

    public String getName() {
        return name;
    }


    public ArrayList<Car> getCars() {
        return cars;
    }

    public void addCar(Car car){

        cars.add(car);
    }
}
