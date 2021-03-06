package com.example.talkcar.Cache;

import com.example.talkcar.Cars.Car;
import com.example.talkcar.Chats.ChattedCarsMap;
import com.example.talkcar.Driver.Driver;

public class ApplicationModel {
    public static Driver currentDriver;
    public static Driver lastDriverSearch;
    public static Car currentCar;
    public static Car lastCarNumberSearch;
    public static String chattedDriverUid;
    public static String currentChatKey;
    public static ChattedCarsMap chattedCarsMap;


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

