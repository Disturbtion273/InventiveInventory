package net.strobel.inventive_inventory.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileHandler {

    public static void write(int element, String filePath) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        jsonArray.add(element);
        jsonObject.add("locked_slots", jsonArray);
        writeToFile(filePath, jsonObject);
    }

    private static void createDirectory() {
        Path path = Paths.get("config/inventive_inventory");
        try {
            Files.createDirectories(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeToFile(String filePath, JsonObject jsonObject) {
        createDirectory();
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
