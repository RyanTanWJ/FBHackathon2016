package com.hackathon.walrus.pigeon;

/**
 * Created by riot94 on 4/9/2016.
 */


public class Message {
    String sender;
    String message;

    public Message(String s, String m){
        sender = s;
        message = m;
    }

    public String getSender(){
        return this.sender;
    }

    public String getMessage(){
        return this.message;
    }
}
