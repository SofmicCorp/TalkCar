package com.example.talkcar;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {

    private String key;
    private ArrayList<Message> messages;
    private boolean allMessagesWereReaded;


    public Chat(){

    }

    public Chat(String id){

        this.key = id;
        messages = new ArrayList<>();
        allMessagesWereReaded = true;

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

    public boolean isAllMessagesWereReaded() {
        return allMessagesWereReaded;
    }

    public void setAllMessagesWereReaded(boolean allMessagesWereReaded) {
        this.allMessagesWereReaded = allMessagesWereReaded;
    }
}
