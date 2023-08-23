package com.oa.learn.utils;


import org.mindrot.jbcrypt.BCrypt;

public class Encryption {
    public static String encodePassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }
}
