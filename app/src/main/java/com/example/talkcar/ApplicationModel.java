package com.example.talkcar;

public class ApplicationModel {
    public static String lastCarNumberSearch;
    public static Driver currentDriver;
    public static Car currentCar;

    public static String getLastCarNumberSearch() {
        return lastCarNumberSearch;
    }

    public static Driver getCurrentDriver() {
        return currentDriver;
    }

    public static void setCurrentDriver(Driver currentDriver) {
        ApplicationModel.currentDriver = currentDriver;
    }

    public static void setLastCarNumberSearch(String lastCarNumberSearch) {
        ApplicationModel.lastCarNumberSearch = lastCarNumberSearch;
    }

    public static Car getCurrentCar() {
        return currentCar;
    }

    public static void setCurrentCar(Car currentCar) {
        ApplicationModel.currentCar = currentCar;
    }
}
