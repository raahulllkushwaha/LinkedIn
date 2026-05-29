package com.rahul.userservice.utils;

import static org.mindrot.jbcrypt.BCrypt.*;

public class Bcrypt {
    public static String hash(String str){
        return hashpw(str, gensalt());
    }

    public static boolean match(String passwordText, String passwordHashed){
        return checkpw(passwordText, passwordHashed);
    }
}
