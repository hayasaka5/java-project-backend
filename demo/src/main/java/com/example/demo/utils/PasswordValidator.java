package com.example.demo.utils;

public class PasswordValidator {
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) return false;
        return password.matches("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}");
    }
}
