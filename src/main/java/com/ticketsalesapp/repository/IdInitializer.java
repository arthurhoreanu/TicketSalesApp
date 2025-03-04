package main.java.com.ticketsalesapp.repository;

import java.io.*;
import java.util.List;

public class IdInitializer {

    public static void initializeGlobalId(List<String> filePaths) {
        int maxId = filePaths.stream()
                .map(IdInitializer::getMaxIdFromFile)
                .max(Integer::compareTo)
                .orElse(0);
        GlobalIdGenerator.initialize(maxId);
    }

    private static int getMaxIdFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            return reader.lines()
                    .map(line -> Integer.parseInt(line.split(",")[0].trim()))
                    .max(Integer::compareTo)
                    .orElse(0);
        } catch (IOException e) {
            System.err.println("Error reading file: " + filePath);
            return 0;
        }
    }
}