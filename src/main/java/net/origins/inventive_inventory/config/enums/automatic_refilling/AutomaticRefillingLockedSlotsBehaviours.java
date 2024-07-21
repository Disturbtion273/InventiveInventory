package net.origins.inventive_inventory.config.enums.automatic_refilling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.Configurable;

public enum AutomaticRefillingLockedSlotsBehaviours implements Configurable {
    IGNORE_LOCKED_SLOTS("Ignore Locked Slots"),
    USE_LOCKED_SLOTS("Use Locked Slots");

    public static final String CONFIG_KEY = "Automatic Refilling Locked Slots Behaviour";
    private final String name;

    AutomaticRefillingLockedSlotsBehaviours(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void toggle() {
        ConfigManager.AR_LS_BEHAVIOUR = values()[(this.ordinal() + 1) % values().length];
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
