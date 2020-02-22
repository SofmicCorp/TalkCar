package com.example.talkcar;

import java.io.Serializable;
import java.util.HashMap;

public class Car implements Serializable {

    private String carNumber;
    private String nickname;
    private String emojiId;
    private HashMap<String,String> hashMap;

    public Car(){

    }

    public Car(String carNumber, String nickname, String emojiId){
        this.carNumber = carNumber;
        this.nickname = nickname;
        this.emojiId = emojiId;
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
}
