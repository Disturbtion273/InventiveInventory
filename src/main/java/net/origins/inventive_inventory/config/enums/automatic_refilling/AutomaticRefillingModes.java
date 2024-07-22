package net.origins.inventive_inventory.config.enums.automatic_refilling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.accessibility.Tooltips;
import net.origins.inventive_inventory.config.enums.Configurable;

public enum AutomaticRefillingModes implements Configurable {
    AUTOMATIC("Automatic"),
    SEMI_AUTOMATIC("Semi-Automatic");

    public static final String CONFIG_KEY = "Automatic Refilling Mode";
    public static final String DISPLAY_NAME = "Mode";
    private final String name;

    AutomaticRefillingModes(String name) {
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
        return Tooltips.AR_MODE;
    }

    @Override
    public void toggle() {
        ConfigManager.AR_MODE = values()[(this.ordinal() + 1) % values().length];
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
