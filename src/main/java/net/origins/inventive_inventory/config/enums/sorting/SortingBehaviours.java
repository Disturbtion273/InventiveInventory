package net.origins.inventive_inventory.config.enums.sorting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.Configurable;

public enum SortingBehaviours implements Configurable {
    SORT_CURSOR_STACK("Sort Cursor Stack"),
    KEEP_CURSOR_STACK("Keep Cursor Stack"),
    AOK_DEPENDENT("AOK-Dependent"),
    AOK_DEPENDENT_INVERTED("AOK-Dependent Inverted");

    static private final String configKey = "Sorting Behaviour";
    private final String name;

    SortingBehaviours(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void toggle() {
        ConfigManager.S_BEHAVIOUR = values()[(this.ordinal() + 1) % values().length];
    }

    public static Configurable get(JsonObject config) {
        JsonElement element = config.get(configKey);
        if (element != null) {
            for (Configurable configurable : values()) {
                if (configurable.getName().equals(element.getAsString())) return configurable;
            }
        }
        return values()[0];
    }
}
