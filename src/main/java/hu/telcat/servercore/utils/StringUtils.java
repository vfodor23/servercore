package hu.telcat.servercore.utils;

import java.util.Random;

public class StringUtils {

    public static String generateRandomString(int length){
        String keys = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for(int i = 0; i < length; i++){
            int index = random.nextInt(keys.length());
            char randomChar = keys.charAt(index);
            sb.append(randomChar);
        }
        return sb.toString();
    }
}
