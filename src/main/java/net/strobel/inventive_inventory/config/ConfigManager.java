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
    public static void initialize() {
        try {
            if (!Files.exists(ConfigManager.PATH)) {
                Files.createDirectories(ConfigManager.PATH);
            }
            if (!Files.exists(LockedSlotsHandler.LOCKED_SLOTS_PATH)) {
                Files.createFile(LockedSlotsHandler.LOCKED_SLOTS_PATH);
            }
            if (!Files.exists(ProfileHandler.PROFILES_PATH)) {
                Files.createFile(ProfileHandler.PROFILES_PATH);
            }
        } catch (IOException ignored) {}
    }
}
