package com.reindefox.homelibrary.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuthorizationUtils {
    public static final String PREFS_USER = "user";

    public static final String PREFS_AUTO_LOGIN = "keep_login";

    /**
     * Применение алгоритма для шифрования пароля
     * @param password исходный пароль
     * @return зашифрованный пароль
     */
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
            return null;
        }
    }
}
