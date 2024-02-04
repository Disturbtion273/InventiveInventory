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

    public static void write(JsonArray array, String filePath, String jsonKey) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(jsonKey, array);
        writeToFile(filePath, jsonObject);
    }

    public static void write(String filePath, List<?> list, String jsonKey) {
        JsonElement array = gson.toJsonTree(list);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(jsonKey, array);
        writeToFile(filePath, jsonObject);
    }

    public static <Type> List<Type> get(String filePath, String jsonKey) {
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filePath)).getAsJsonObject();
            JsonArray array = jsonObject.get(jsonKey).getAsJsonArray();
            List<Type> list = new ArrayList<>();
            for (JsonElement element: array) {
                if (array.get(0).getAsJsonPrimitive().isNumber()) {
                    list.add((Type) Integer.valueOf(element.getAsInt()));
                } else if (array.get(0).getAsJsonPrimitive().isString()) {
                    list.add((Type) String.valueOf(element.getAsString()));
                } else if (array.get(0).getAsJsonPrimitive().isBoolean()) {
                    list.add((Type) Boolean.valueOf(element.getAsBoolean()));
                }
            }
            return list;
        } catch (IllegalStateException e) {
            return new ArrayList<>();
        } catch (ClassCastException | FileNotFoundException ignored) {}
        return null;
    }

    private static void writeToFile(String filePath, JsonObject jsonObject) {
        try (FileWriter file = new FileWriter(filePath)) {
            file.write(gson.toJson(jsonObject));
        } catch (IOException ignored) {}
    }
}
