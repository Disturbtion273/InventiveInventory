package net.strobel.inventive_inventory.config;

import net.fabricmc.loader.api.FabricLoader;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    public static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve(InventiveInventory.MOD_ID);
    public static Mode AUTOMATIC_REFILLING = Mode.STANDARD;

    public static void initialize() throws IOException {
        Files.createDirectories(ConfigManager.PATH);
        createFileIfNotExists(LockedSlotsHandler.LOCKED_SLOTS_PATH);
        createFileIfNotExists(ProfileHandler.PROFILES_PATH);
    }

    private static void createFileIfNotExists(Path path) throws IOException {
        if (!Files.exists(path)) {
            Files.createFile(path);
        }
    }
}
