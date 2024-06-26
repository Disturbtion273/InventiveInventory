package net.origins.inventive_inventory.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingBehaviours;
import net.origins.inventive_inventory.config.enums.sorting.SortingBehaviours;
import net.origins.inventive_inventory.config.enums.sorting.SortingModes;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.util.FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.json";

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(InventiveInventory.MOD_ID);
    private static final Path CONFIG_FILE_PATH = CONFIG_PATH.resolve(CONFIG_FILE);

    public static Configurable SORTING_MODE;
    public static Configurable SORTING_BEHAVIOUR;
    public static Configurable AUTOMATIC_REFILLING_MODE;
    public static Configurable AUTOMATIC_REFILLING_BEHAVIOUR;

    public static void init() throws IOException {
        Files.createDirectories(CONFIG_PATH);
        FileHandler.createFile(CONFIG_FILE_PATH);
        FileHandler.createFile(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        initConfig();
        save();
    }

    public static void save() {
        JsonObject config = new JsonObject();
        config.addProperty("Sorting Mode", SORTING_MODE.getName());
        config.addProperty("Sorting Behaviour", SORTING_BEHAVIOUR.getName());
        config.addProperty("Automatic Refilling Mode", AUTOMATIC_REFILLING_MODE.getName());
        config.addProperty("Automatic Refilling Behaviour", AUTOMATIC_REFILLING_BEHAVIOUR.getName());

        FileHandler.write(CONFIG_FILE_PATH, config);
    }

    private static void initConfig() {
        JsonObject config = FileHandler.get(CONFIG_FILE_PATH).isJsonObject() ? FileHandler.get(CONFIG_FILE_PATH).getAsJsonObject() : new JsonObject();
        SORTING_MODE = SortingModes.get(config);
        SORTING_BEHAVIOUR = SortingBehaviours.get(config);
        AUTOMATIC_REFILLING_MODE = AutomaticRefillingModes.get(config);
        AUTOMATIC_REFILLING_BEHAVIOUR = AutomaticRefillingBehaviours.get(config);
    }
}
