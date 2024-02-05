package net.strobel.inventive_inventory.features.profiles;

import net.strobel.inventive_inventory.config.ConfigManager;

import java.nio.file.Path;

public class ProfileHandler {
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = ConfigManager.PATH.resolve(PROFILES_FILE);

}
