package net.origins.inventive_inventory.config.automatic_refilling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.config.ConfigManager;

public enum AutomaticRefillingBehaviours {
    IGNORE_LOCKED_SLOTS("Ignore Locked Slots"),
    REFILL_FROM_LOCKED_SLOTS("Refill From Locked Slots");

    private final String name;

    AutomaticRefillingBehaviours(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static AutomaticRefillingBehaviours get(JsonObject config) {
        JsonElement automaticRefillingBehaviour = config.get("Automatic Refilling Behaviour");
        if (automaticRefillingBehaviour != null) {
            for (AutomaticRefillingBehaviours behaviour : values()) {
                if (behaviour.name.equals(automaticRefillingBehaviour.getAsString())) {
                    return behaviour;
                }
            }
        }
        return IGNORE_LOCKED_SLOTS;
    }

    public void toggle() {
        ConfigManager.AUTOMATIC_REFILLING_BEHAVIOUR = values()[(this.ordinal() + 1) % values().length];
    }
}
