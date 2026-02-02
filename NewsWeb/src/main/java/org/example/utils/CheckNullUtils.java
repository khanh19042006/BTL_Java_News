package org.example.utils;

import org.example.dto.UserDTO;

public class CheckNullUtils {

    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    // Trả về true nếu có bất kỳ field nào null hoặc rỗng
    public static boolean checkNullUser(UserDTO user) {

        if (user == null) return true;

        return isBlank(user.getId()) ||
                isBlank(user.getUsername()) ||
                isBlank(user.getEmail()) ||
                isBlank(user.getPassword()) ||
                isBlank(user.getCreated_at()) ||
                isBlank(user.getRole());
    }

    // Trả về tên field bị null hoặc rỗng (dùng để debug)
    public static String getNullOrBlankFieldUser(UserDTO user) {

        if (user == null) return "User object is null";

        if (isBlank(user.getId())) return "id";
        if (isBlank(user.getUsername())) return "username";
        if (isBlank(user.getEmail())) return "email";
        if (isBlank(user.getPassword())) return "password";
        if (isBlank(user.getCreated_at())) return "created_at";
        if (isBlank(user.getRole())) return "role";

        return null;
    }
}
