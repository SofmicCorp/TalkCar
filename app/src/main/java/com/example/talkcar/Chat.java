package com.example.talkcar;

import java.util.ArrayList;

public class Chat {

    private String key;
    private String emojiId;
    private ArrayList<Message> messages;

    public Chat(){

    }

    public Chat(String id,String emojiId){

        this.key = id;
        this.emojiId = emojiId;
        messages = new ArrayList<>();

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

    public String getEmojiId() {
        return emojiId;
    }
}
