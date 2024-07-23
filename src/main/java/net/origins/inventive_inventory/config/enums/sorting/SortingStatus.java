package net.origins.inventive_inventory.config.enums.sorting;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.config.accessibility.Tooltips;
import net.origins.inventive_inventory.config.enums.Configurable;

public enum SortingStatus implements Configurable {
    ENABLED("Enabled", Formatting.GREEN),
    DISABLED("Disabled", Formatting.RED);

    public static final String CONFIG_KEY = "Sorting";
    public static final String DISPLAY_NAME = "Sorting";
    private final String name;
    private final Style style;

    SortingStatus(String name, Formatting color) {
        this.name = name;
        this.style = Style.EMPTY.withColor(color);
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
        return Tooltips.STATUS;
    }

    @Override
    public Style getStyle() {
        return this.style;
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

