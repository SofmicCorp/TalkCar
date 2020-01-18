package com.example.talkcar;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class Driver implements Serializable {

    private String firstname;
    private String lastname;
    private String email;
    private ArrayList<Car> cars;

    public Driver(){

    }

    public Driver(String firstname, String lastname,String email){
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        cars = new ArrayList<>();
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
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
