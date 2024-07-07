package net.origins.inventive_inventory.features.profiles;

import net.origins.inventive_inventory.config.ConfigManager;

import java.nio.file.Path;

public class ProfileHandler {
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = ConfigManager.CONFIG_PATH.resolve(PROFILES_FILE);


}
