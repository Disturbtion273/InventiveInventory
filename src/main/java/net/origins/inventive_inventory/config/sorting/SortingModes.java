package net.origins.inventive_inventory.config.sorting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.config.ConfigManager;

public enum SortingModes {
    NAME("Name"),
    ITEM_TYPE("Item Type");

    private final String name;

    SortingModes(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static SortingModes get(JsonObject config) {
        JsonElement sortingMode = config.get("Sorting Behaviour");
        if (sortingMode != null) {
            for (SortingModes mode : values()) {
                if (mode.name.equals(sortingMode.getAsString())) {
                    return mode;
                }
            }
        }
        return ITEM_TYPE;
    }

    public void toggle() {
        ConfigManager.SORTING_MODE = values()[(this.ordinal() + 1) % values().length];
    }
}
