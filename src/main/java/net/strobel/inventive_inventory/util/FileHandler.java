package net.strobel.inventive_inventory.util;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHandler {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void write(String filePath, String jsonKey, JsonArray array) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(jsonKey, array);
        writeToFile(filePath, jsonObject);
    }

    public static void write(String filePath, String jsonKey, List<?> list) {
        JsonElement array = gson.toJsonTree(list);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(jsonKey, array);
        writeToFile(filePath, jsonObject);
    }

    public static <T> List<T> get(String filePath, String jsonKey) {
        List<T> list = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
            JsonArray array = jsonObject.get(jsonKey).getAsJsonArray();
            if (!array.isEmpty()) {
                for (JsonElement element: array) {
                    if (element.getAsJsonPrimitive().isNumber()) {
                        list.add((T) Integer.valueOf(element.getAsInt()));
                    } else if (element.getAsJsonPrimitive().isString()) {
                        list.add((T) String.valueOf(element.getAsString()));
                    } else if (element.getAsJsonPrimitive().isBoolean()) {
                        list.add((T) Boolean.valueOf(element.getAsBoolean()));
                    }
                }
            }
        } catch (IllegalStateException | ClassCastException | FileNotFoundException ignored) {}
        return list;
    }


    private static void writeToFile(String filePath, JsonObject jsonObject) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(gson.toJson(jsonObject));
        } catch (IOException ignored) {}
    }
}
