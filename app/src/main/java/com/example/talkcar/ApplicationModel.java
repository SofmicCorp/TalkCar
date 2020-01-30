package com.example.talkcar;

public class ApplicationModel {
    public static Driver currentDriver;
    public static Car currentCar;
    public static Car lastCarNumberSearch;

    public static Driver getCurrentDriver() {
        return currentDriver;
    }

    public static void setCurrentDriver(Driver currentDriver) {
        ApplicationModel.currentDriver = currentDriver;
    }

    public static Car getCurrentCar() {
        return currentCar;
    }

    public static void setCurrentCar(Car currentCar) {
        ApplicationModel.currentCar = currentCar;
    }

    public static Car getLastCarNumberSearch() {
        return lastCarNumberSearch;
    }

    public static void setLastCarNumberSearch(Car lastCarNumberSearch) {
        ApplicationModel.lastCarNumberSearch = lastCarNumberSearch;
    }
}

