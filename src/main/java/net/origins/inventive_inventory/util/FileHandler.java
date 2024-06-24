package net.origins.inventive_inventory.util;

import com.google.gson.*;
import net.origins.inventive_inventory.InventiveInventory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileHandler {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createFile(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    public static JsonElement get(Path path) {
        JsonElement jsonElement = new JsonObject();
        try {
            jsonElement = JsonParser.parseReader(new FileReader(path.toFile()));
        } catch (FileNotFoundException | IllegalStateException ignored) {}
        return jsonElement;
    }

    public static void write(Path path, JsonElement jsonElement) {
        try (FileWriter file = new FileWriter(path.toFile())) {
            file.write(gson.toJson(jsonElement));
        } catch (IOException e) {
            InventiveInventory.LOGGER.error("Could not write configurations to file!");
        }
    }
}
