package com.example.talkcar;

public class Car {

    private String carNumber;
    private String nickname;
    private int emojiId;

    public Car(){

    }


    public Car(String carNumber, String nickname, int emojiId){
        this.carNumber = carNumber;
        this.nickname = nickname;
        this.emojiId = emojiId;
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

    public void setEmojiId(int emojiId) {
        this.emojiId = emojiId;
    }

    public String getNickname() {
        return nickname;
    }

    public int getEmojiId() {
        return emojiId;
    }

}
