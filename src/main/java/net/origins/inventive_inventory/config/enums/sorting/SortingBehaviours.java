package net.origins.inventive_inventory.config.enums.sorting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.accessibility.Tooltips;
import net.origins.inventive_inventory.config.enums.Configurable;

public enum SortingBehaviours implements Configurable {
    SORT_CURSOR_STACK("Sort Cursor Stack"),
    KEEP_CURSOR_STACK("Keep Cursor Stack"),
    AOK_DEPENDENT("AOK-Dependent"),
    AOK_DEPENDENT_INVERTED("AOK-Dependent Inverted");

    public static final String CONFIG_KEY = "Sorting Behaviour";
    public static final String DISPLAY_NAME = "Behaviour";
    private final String name;

    SortingBehaviours(String name) {
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
    public Text getTooltip() {
        return Tooltips.S_BEHAVIOUR;
    }

    @Override
    public void toggle() {
        ConfigManager.S_BEHAVIOUR = values()[(this.ordinal() + 1) % values().length];
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
