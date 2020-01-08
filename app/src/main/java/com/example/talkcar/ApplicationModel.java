package com.example.talkcar;



public class ApplicationModel {
    private String lastCarNumberSearch;
    private Driver currentDriver;
    private Car currentCar;

    public ApplicationModel(){

    }



    public String getLastCarNumberSearch() {
        return lastCarNumberSearch;
    }

    public Driver getCurrentDriver() {
        return currentDriver;
    }

    public void setCurrentDriver(Driver currentDriver) {
        this.currentDriver = currentDriver;
    }

    public void setLastCarNumberSearch(String lastCarNumberSearch) {
        this.lastCarNumberSearch = lastCarNumberSearch;
    }

    public Car getCurrentCar() {
        return currentCar;
    }

    public void setCurrentCar(Car currentCar) {
        this.currentCar = currentCar;
    }
}
