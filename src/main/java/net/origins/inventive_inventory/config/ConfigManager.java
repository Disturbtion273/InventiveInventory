package net.origins.inventive_inventory.config;

import com.google.gson.JsonObject;
import net.fabricmc.loader.api.FabricLoader;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingStatus;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingLockedSlotsBehaviours;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingToolBehaviours;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesStatus;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesLoadMode;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesLockedSlotsBehaviours;
import net.origins.inventive_inventory.config.enums.sorting.SortingBehaviours;
import net.origins.inventive_inventory.config.enums.sorting.SortingModes;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.config.enums.sorting.SortingStatus;
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

    public static Configurable SORTING;
    public static Configurable S_MODE;
    public static Configurable S_BEHAVIOUR;
    public static Configurable AUTOMATIC_REFILLING;
    public static Configurable AR_MODE;
    public static Configurable AR_LS_BEHAVIOUR;
    public static Configurable AR_TOOL_BEHAVIOUR;
    public static Configurable PROFILES;
    public static Configurable P_LOAD_MODE;
    public static Configurable P_LS_BEHAVIOUR;

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
        config.addProperty(SortingStatus.CONFIG_KEY, SORTING.getName());
        config.addProperty(SortingModes.CONFIG_KEY, S_MODE.getName());
        config.addProperty(SortingBehaviours.CONFIG_KEY, S_BEHAVIOUR.getName());
        config.addProperty(AutomaticRefillingStatus.CONFIG_KEY, AUTOMATIC_REFILLING.getName());
        config.addProperty(AutomaticRefillingModes.CONFIG_KEY, AR_MODE.getName());
        config.addProperty(AutomaticRefillingLockedSlotsBehaviours.CONFIG_KEY, AR_LS_BEHAVIOUR.getName());
        config.addProperty(AutomaticRefillingToolBehaviours.CONFIG_KEY, AR_TOOL_BEHAVIOUR.getName());
        config.addProperty(ProfilesStatus.CONFIG_KEY, PROFILES.getName());
        config.addProperty(ProfilesLoadMode.CONFIG_KEY, P_LOAD_MODE.getName());
        config.addProperty(ProfilesLockedSlotsBehaviours.CONFIG_KEY, P_LS_BEHAVIOUR.getName());

        FileHandler.write(CONFIG_FILE_PATH, config);
    }

    private static void initConfig() {
        JsonObject config = FileHandler.get(CONFIG_FILE_PATH).isJsonObject() ? FileHandler.get(CONFIG_FILE_PATH).getAsJsonObject() : new JsonObject();
        SORTING = SortingStatus.get(config);
        S_MODE = SortingModes.get(config);
        S_BEHAVIOUR = SortingBehaviours.get(config);
        AUTOMATIC_REFILLING = AutomaticRefillingStatus.get(config);
        AR_MODE = AutomaticRefillingModes.get(config);
        AR_LS_BEHAVIOUR = AutomaticRefillingLockedSlotsBehaviours.get(config);
        AR_TOOL_BEHAVIOUR = AutomaticRefillingToolBehaviours.get(config);
        PROFILES = ProfilesStatus.get(config);
        P_LOAD_MODE = ProfilesLoadMode.get(config);
        P_LS_BEHAVIOUR = ProfilesLockedSlotsBehaviours.get(config);
    }
}
