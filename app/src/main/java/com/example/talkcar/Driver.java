package com.example.talkcar;

import java.util.ArrayList;

public class Driver {

    private String email;
    private ArrayList<Car> cars;


    public Driver(String email){
        this.email = email;
        cars = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<Car> getCars() {
        return cars;
    }

    public void addCar(Car car){

        cars.add(car);
    }

    public String getCarNumber(int index){
        return cars.get(index).getCarNumber();
    }



}
