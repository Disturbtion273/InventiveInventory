package net.strobel.inventive_inventory.config;

import net.fabricmc.loader.api.FabricLoader;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.util.FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ConfigManager {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(InventiveInventory.MOD_ID);
    public static final String SETTINGS_FILE = "settings.properties";
    public static final Path SETTINGS_PATH = ConfigManager.PATH.resolve(SETTINGS_FILE);
    public static Mode AUTOMATIC_REFILLING;

    public static void initialize() throws IOException {
        Files.createDirectories(ConfigManager.PATH);
        createFileIfNotExists(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        createFileIfNotExists(ProfileHandler.PROFILES_PATH);
        createFileIfNotExists(ConfigManager.SETTINGS_PATH);
        initializeSettings();
        saveSettings();
    }

    public static void saveSettings() {
        FileHandler.write(SETTINGS_PATH, "AutomaticRefillingMode", AUTOMATIC_REFILLING.toString());
    }

    private static void createFileIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    private static void initializeSettings() {
        Properties properties = FileHandler.getProperties(SETTINGS_PATH);
        String automaticRefillingMode = properties.getProperty("AutomaticRefillingMode");
        if (automaticRefillingMode != null) {
            if (automaticRefillingMode.equals(Mode.STANDARD.name())) AUTOMATIC_REFILLING = Mode.STANDARD;
            if (automaticRefillingMode.equals(Mode.INVERTED.name())) AUTOMATIC_REFILLING = Mode.INVERTED;
        } else {
            AUTOMATIC_REFILLING = Mode.STANDARD;
        }
    }
}
