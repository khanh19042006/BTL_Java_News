package org.example.utils;

public class TextUtils {

    // chặn khởi tạo
    private TextUtils() {}

    // Hàm viết hoa chữ cái đầu mỗi từ
    public static String capitalizeFully(String str) {
        if (str == null || str.isBlank()) return str;

        String[] words = str.toLowerCase().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String w : words) {
            sb.append(Character.toUpperCase(w.charAt(0)))
                    .append(w.substring(1))
                    .append(" ");
        }

        return sb.toString().trim();
    }

}
