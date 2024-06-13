package net.strobel.inventive_inventory.util;

import com.google.gson.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class FileHandler {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void write(Path filePath, String jsonKey, List<?> list) {
        JsonElement array = gson.toJsonTree(list);
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(jsonKey, array);
        writeToFile(filePath, jsonObject);
    }

    public static void write(Path filePath, JsonObject jsonObject) {
        writeToFile(filePath, jsonObject);
    }

    public static void write(Path filePath, String key, String value) {writeToFile(filePath, key, value);}

    public static void write(Path filePath, Map<String, String> properties) {writeToFile(filePath, properties);}

    public static <T> List<T> get(Path filePath, String jsonKey) {
        List<T> list = new ArrayList<>();
        try {
            JsonObject jsonObject = JsonParser.parseReader(new FileReader(filePath.toFile())).getAsJsonObject();
            JsonArray array = jsonObject.get(jsonKey).getAsJsonArray();
            if (!array.isEmpty()) {
                for (JsonElement element : array) {
                    if (element.getAsJsonPrimitive().isNumber()) {
                        list.add((T) Integer.valueOf(element.getAsInt()));
                    } else if (element.getAsJsonPrimitive().isString()) {
                        list.add((T) String.valueOf(element.getAsString()));
                    } else if (element.getAsJsonPrimitive().isBoolean()) {
                        list.add((T) Boolean.valueOf(element.getAsBoolean()));
                    }
                }
            }
        } catch (FileNotFoundException | IllegalStateException | ClassCastException ignored) {}
        return list;
    }

    public static JsonObject getJsonObject(Path filePath, String jsonKey) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = JsonParser.parseReader(new FileReader(filePath.toFile())).getAsJsonObject().get(jsonKey).getAsJsonObject();
        } catch (FileNotFoundException | IllegalStateException | ClassCastException | NullPointerException ignored) {
        }
        return jsonObject;
    }

    public static JsonObject getJsonFile(Path filePath) {
        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject = JsonParser.parseReader(new FileReader(filePath.toFile())).getAsJsonObject();
        } catch (FileNotFoundException | IllegalStateException ignored) {}
        return jsonObject;
    }

    public static Properties getProperties(Path filePath) {
        Properties properties = new Properties();
        try (FileReader reader = new FileReader(filePath.toFile())) {
            properties.load(reader);
        } catch (IOException ignored) {
        }
        return properties;
    }

    private static void writeToFile(Path filePath, JsonObject jsonObject) {
        try (FileWriter file = new FileWriter(filePath.toFile())) {
            file.write(gson.toJson(jsonObject));
        } catch (IOException ignored) {
        }
    }

    private static void writeToFile(Path filePath, String key, String value) {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        try (FileWriter file = new FileWriter(filePath.toFile())) {
            properties.store(file, null);
        } catch (IOException ignored) {
        }
    }

    private static void writeToFile(Path filePath, Map<String, String> properties_) {
        Properties properties = new Properties();
        for (String key : properties_.keySet()) {
            properties.setProperty(key, properties_.get(key));
        }
        try (FileWriter file = new FileWriter(filePath.toFile())) {
            properties.store(file, null);
        } catch (IOException ignored) {
        }
    }
}
