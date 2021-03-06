package com.example.talkcar.Cars;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class Car implements Serializable {

    private String carNumber;
    private String nickname;
    private String emojiId;
    private String driverUid;
    private HashMap<String,String> hashMap;


    public Car(){

    }

    public Car(String carNumber, String nickname, String emojiId,String driverUid){
        this.carNumber = carNumber;
        this.nickname = nickname;
        this.emojiId = emojiId;
        this.driverUid = driverUid;
        hashMap = new HashMap<>();
    }


    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setEmojiId(String emojiId) {
        this.emojiId = emojiId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getEmojiId() {
        return emojiId;
    }

    @Override
    public String toString() {
        return nickname;
    }

    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    public String getDriverUid() {
        return driverUid;
    }

    public void setDriverUid(String driverUid) {
        this.driverUid = driverUid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Objects.equals(carNumber, car.carNumber);
    }


}
