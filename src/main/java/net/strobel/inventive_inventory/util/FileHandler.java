package net.strobel.inventive_inventory.util;

import com.google.gson.*;
import net.strobel.inventive_inventory.InventiveInventoryClient;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {

    public static void write(JsonArray array, String filePath, String json_key) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(json_key, array);
        writeToFile(filePath, jsonObject);
    }

    public static void createConfigs() {
        try {
            Files.createDirectories(Path.of(InventiveInventoryClient.CONFIG_PATH));
            Files.createFile(Path.of(InventiveInventoryClient.CONFIG_PATH + "locked_slots.json"));
        } catch (IOException ignored) {}
    }

    private static void writeToFile(String filePath, JsonObject jsonObject) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(jsonObject.toString());
        } catch (IOException ignored) {}
    }

    public static JsonArray get(String filePath) {
        JsonArray array = new JsonArray();
        try {
            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(new FileReader(filePath));
            array = (JsonArray) jsonObject.get("locked_slots");
        } catch (FileNotFoundException | ClassCastException ignored) {}
        return array;
    }

}
