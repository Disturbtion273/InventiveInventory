package net.origins.inventive_inventory.config.enums.automatic_refilling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.Configurable;

public enum AutomaticRefillingStatus implements Configurable {
    ENABLED("Enabled", Formatting.GREEN),
    DISABLED("Disabled", Formatting.RED);

    static private final String configKey = "Automatic Refilling";
    private final String name;
    private final Style style;

    AutomaticRefillingStatus(String name, Formatting color) {
        this.name = name;
        this.style = Style.EMPTY.withColor(color);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Style getStyle() {
        return this.style;
    }

    @Override
    public void toggle() {
        ConfigManager.AUTOMATIC_REFILLING = values()[(this.ordinal() + 1) % values().length];
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

