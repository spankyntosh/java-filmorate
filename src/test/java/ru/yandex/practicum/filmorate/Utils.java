package ru.yandex.practicum.filmorate;

import java.io.BufferedReader;
import java.io.FileReader;

public class Utils {

    static String readFile(String path) {
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            String lineSeparator = System.getProperty("line.separator");
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(lineSeparator);
            }
        } catch (Exception e) {

        }
        return stringBuilder.toString();
    }
}
