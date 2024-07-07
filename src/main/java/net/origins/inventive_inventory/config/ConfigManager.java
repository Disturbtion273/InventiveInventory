package net.origins.inventive_inventory.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingBehaviours;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingToolBehaviours;
import net.origins.inventive_inventory.config.enums.sorting.SortingBehaviours;
import net.origins.inventive_inventory.config.enums.sorting.SortingModes;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.util.FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private static final String CONFIG_FILE = "config.json";

    public static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve(InventiveInventory.MOD_ID);
    private static final Path CONFIG_FILE_PATH = CONFIG_PATH.resolve(CONFIG_FILE);

    public static Configurable S_MODE;
    public static Configurable S_BEHAVIOUR;
    public static Configurable AR_MODE;
    public static Configurable AR_LS_BEHAVIOUR;
    public static Configurable AR_TOOL_BEHAVIOUR;

    public static void init() throws IOException {
        Files.createDirectories(CONFIG_PATH);
        FileHandler.createFile(CONFIG_FILE_PATH);
        FileHandler.createFile(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        FileHandler.createFile(ProfileHandler.PROFILES_PATH);
        initConfig();
        save();
    }

    public static void save() {
        JsonObject config = new JsonObject();
        config.addProperty("Sorting Mode", S_MODE.getName());
        config.addProperty("Sorting Behaviour", S_BEHAVIOUR.getName());
        config.addProperty("Automatic Refilling Mode", AR_MODE.getName());
        config.addProperty("Automatic Refilling Behaviour", AR_LS_BEHAVIOUR.getName());
        config.addProperty("Automatic Refilling Tool Behaviour", AR_TOOL_BEHAVIOUR.getName());

        FileHandler.write(CONFIG_FILE_PATH, config);
    }

    private static void initConfig() {
        JsonObject config = FileHandler.get(CONFIG_FILE_PATH).isJsonObject() ? FileHandler.get(CONFIG_FILE_PATH).getAsJsonObject() : new JsonObject();
        S_MODE = SortingModes.get(config);
        S_BEHAVIOUR = SortingBehaviours.get(config);
        AR_MODE = AutomaticRefillingModes.get(config);
        AR_LS_BEHAVIOUR = AutomaticRefillingBehaviours.get(config);
        AR_TOOL_BEHAVIOUR = AutomaticRefillingToolBehaviours.get(config);
    }
}
