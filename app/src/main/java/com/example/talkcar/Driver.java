package com.example.talkcar;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Driver implements Serializable {

    private String name;
    private String uId;
    private ArrayList<Car> cars;

    public Driver(){

    }

    public Driver(String name,String uId){
        this.name = name;
        this.uId = uId;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public void setCars(ArrayList<Car> cars) {
        this.cars = cars;
    }
}
