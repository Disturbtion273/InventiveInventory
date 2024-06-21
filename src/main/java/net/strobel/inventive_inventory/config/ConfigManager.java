package net.strobel.inventive_inventory.config;

import net.fabricmc.loader.api.FabricLoader;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.util.FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigManager {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(InventiveInventory.MOD_ID);
    public static final String SETTINGS_FILE = "settings.properties";
    public static final Path SETTINGS_PATH = ConfigManager.PATH.resolve(SETTINGS_FILE);
    public static Mode AUTOMATIC_REFILLING;
    public static Mode SORTING;
    public static List<Mode> PROFILE_FAST_LOADING = new ArrayList<>(Collections.nCopies(9, Mode.STANDARD));

    public static void initialize() throws IOException {
        Files.createDirectories(ConfigManager.PATH);
        createFileIfNotExists(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        createFileIfNotExists(ProfileHandler.PROFILES_PATH);
        createFileIfNotExists(ConfigManager.SETTINGS_PATH);
        initializeSettings();
        save();
    }

    public static void save() {
        Map<String, String> properties = new HashMap<>();
        properties.put("AutomaticRefillingMode", AUTOMATIC_REFILLING.toString());
        properties.put("SortingMode", SORTING.toString());
        int i = 1;
        for (Mode mode : PROFILE_FAST_LOADING) {
            properties.put("ProfileLoadingMode_" + i, mode.toString());
            i++;
        }
        FileHandler.write(SETTINGS_PATH, properties);
    }

    private static void createFileIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }

    private static void initializeSettings() {
        Properties properties = FileHandler.getProperties(SETTINGS_PATH);
        String automaticRefillingMode = properties.getProperty("AutomaticRefillingMode");
        String sortingMode = properties.getProperty("SortingMode");
        if (automaticRefillingMode != null) {
            if (automaticRefillingMode.equals(Mode.STANDARD.toString())) AUTOMATIC_REFILLING = Mode.STANDARD;
            if (automaticRefillingMode.equals(Mode.INVERTED.toString())) AUTOMATIC_REFILLING = Mode.INVERTED;
        } else {
            AUTOMATIC_REFILLING = Mode.STANDARD;
        }
        if (sortingMode != null) {
            if (sortingMode.equals(Mode.NAME.toString())) SORTING = Mode.NAME;
            if (sortingMode.equals(Mode.ITEM_TYPE.toString())) SORTING = Mode.ITEM_TYPE;
        } else {
            SORTING = Mode.NAME;
        }
        for (int i = 0; i < 9; i++) {
            String mode = properties.getProperty("ProfileLoadingMode_" + (i + 1));
            if (mode != null) {
                if (mode.equals(Mode.STANDARD.toString())) PROFILE_FAST_LOADING.set(i, Mode.STANDARD);
                if (mode.equals(Mode.FAST_LOAD.toString())) PROFILE_FAST_LOADING.set(i, Mode.FAST_LOAD);
            } else {
                PROFILE_FAST_LOADING.set(i, Mode.STANDARD);
            }
        }
    }
}
