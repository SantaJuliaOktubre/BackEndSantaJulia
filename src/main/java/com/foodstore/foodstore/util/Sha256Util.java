package com.foodstore.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Sha256Util {
    public static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] dig = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder();
            for (byte b : dig) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar contraseña", e);
        }
    }
}
