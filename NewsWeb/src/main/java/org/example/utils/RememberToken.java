package org.example.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class RememberToken {
    private RememberToken(){};

    public static void saveTokenToLocal(String tokenId) {

        try {
            Path dir = Path.of(System.getProperty("user.home"), ".newsapp");
            Files.createDirectories(dir);

            Path file = dir.resolve("remember.dat");

            Files.writeString(file, tokenId,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getTokenFromLocal() {

        try {
            Path file = Path.of(
                    System.getProperty("user.home"),
                    ".newsapp",
                    "remember.dat"
            );

            if (Files.exists(file)) {
                return Files.readString(file);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
