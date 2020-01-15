package com.example.talkcar;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 implements Hashable{

    //When using this function, the key to every driver in firebase can be the hashed email make the key uniqe.
    //We are doing this because key can not contain @ / . / etc...
    public String hash(String s) {

        MessageDigest m = null;

        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.update(s.getBytes(),0,s.length());
        String hash = new BigInteger(1, m.digest()).toString(16);
        return hash;
    }
}
