package net.origins.inventive_inventory.config.enums.profiles;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.Configurable;

public enum ProfilesLoadMode implements Configurable {
    NORMAL("Normal"),
    FAST_LOAD("Fast Load");

    public static final String CONFIG_KEY = "Profile Load Mode";
    public static final String DISPLAY_NAME = "Load Mode";
    private final String name;

    ProfilesLoadMode(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public void toggle() {
        ConfigManager.P_LS_BEHAVIOUR = values()[(this.ordinal() + 1) % values().length];
    }

    public static Configurable get(JsonObject config) {
        JsonElement element = config.get(CONFIG_KEY);
        if (element != null) {
            for (Configurable configurable : values()) {
                if (configurable.getName().equals(element.getAsString())) return configurable;
            }
        }
        return values()[0];
    }
}
