package org.example.utils;

import java.util.regex.Pattern;

public class EmailUtils {

    private static final String EMAIL_REGEX =
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    private EmailUtils() {}

    public static boolean isValid(String email) {
        if (email == null) return false;
        return Pattern.matches(EMAIL_REGEX, email);
    }
}
