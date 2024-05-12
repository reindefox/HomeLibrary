package com.reindefox.homelibrary.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthorizationUtils {
    public static final String prefsUser = "user";

    /**
     * Максимальная длина имени пользователя и пароля
     */
    public static final int MAX_USER_DATA_LENGTH = 32;

    public static String applySHA256(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
