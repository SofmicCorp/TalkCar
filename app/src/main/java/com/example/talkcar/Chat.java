package com.example.talkcar;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {

    private String key;
    private ArrayList<Message> messages;
    private boolean someMessageWereNotRead;


    public Chat(){

    }

    public Chat(String id){

        this.key = id;
        messages = new ArrayList<>();
        someMessageWereNotRead = false;

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public void addMessage(Message message){
        messages.add(message);
    }

    public boolean isSomeMessageWereNotRead() {
        return someMessageWereNotRead;
    }

    public void setSomeMessageWereNotRead(boolean someMessageWereNotRead) {
        this.someMessageWereNotRead = someMessageWereNotRead;
    }
}
