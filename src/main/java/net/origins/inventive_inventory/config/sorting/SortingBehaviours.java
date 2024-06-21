package net.origins.inventive_inventory.config.sorting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.origins.inventive_inventory.config.ConfigManager;

public enum SortingBehaviours {
    SORT_CURSOR_STACK("Sort Cursor Stack"),
    KEEP_CURSOR_STACK("Keep Cursor Stack"),
    AOK_DEPENDENT("AOK-Dependent");

    private final String name;

    SortingBehaviours(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static SortingBehaviours get(JsonObject config) {
        JsonElement sortingBehaviour = config.get("Sorting Behaviour");
        if (sortingBehaviour != null) {
            for (SortingBehaviours behaviour : values()) {
                if (behaviour.name.equals(sortingBehaviour.getAsString())) {
                    return behaviour;
                }
            }
        }
        return SORT_CURSOR_STACK;
    }

    public void toggle() {
        ConfigManager.SORTING_BEHAVIOUR = values()[(this.ordinal() + 1) % values().length];
    }
}
