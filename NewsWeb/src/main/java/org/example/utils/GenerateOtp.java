package org.example.utils;

public class GenerateOtp {
    public static String genOtp(){
        int otpNumber = (int) (Math.random() * 900000) + 100000;
        String otpCode = String.valueOf(otpNumber);

        return otpCode;
    }
}
