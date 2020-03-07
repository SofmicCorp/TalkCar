package com.example.talkcar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

public class ApplicationModel {
    public static Driver currentDriver;
    public static Driver lastDriverSearch;
    public static Car currentCar;
    public static Car lastCarNumberSearch;
    public static HashMap<Car, String > allChattedCars;
    public static ArrayList<Car> allChattedCarArray;
    public static String chattedDriverUid;
    public static String currentChatKey;


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

    public static Driver getLastDriverSearch() {
        return lastDriverSearch;
    }

    public static void setLastDriverSearch(Driver lastDriverSearch) {
        ApplicationModel.lastDriverSearch = lastDriverSearch;
    }

    public static String getChattedDriverUid() {
        return chattedDriverUid;
    }

    public static void setChattedDriverUid(String chattedDriverUid) {
        ApplicationModel.chattedDriverUid = chattedDriverUid;
    }
}

