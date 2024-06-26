package net.origins.inventive_inventory.config.automatic_refilling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.config.ConfigManager;

public enum AutomaticRefillingModes {
    STANDARD("Standard"),
    INVERTED("Inverted");

    private final String name;

    AutomaticRefillingModes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static AutomaticRefillingModes get(JsonObject config) {
        JsonElement automaticRefillingMode = config.get("Automatic Refilling Mode");
        if (automaticRefillingMode != null) {
            for (AutomaticRefillingModes mode : values()) {
                if (mode.name.equals(automaticRefillingMode.getAsString())) {
                    return mode;
                }
            }
        }
        return STANDARD;
    }

    public void toggle() {
        ConfigManager.AUTOMATIC_REFILLING_MODE = values()[(this.ordinal() + 1) % values().length];
    }
}
