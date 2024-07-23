package net.origins.inventive_inventory.config.enums.automatic_refilling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.accessibility.Tooltips;
import net.origins.inventive_inventory.config.enums.Configurable;

public enum AutomaticRefillingToolBehaviours implements Configurable {
    MATERIAL("Material"),
    HEALTH("Health");

    public static final String CONFIG_KEY = "Automatic Refilling Tool Behaviour";
    public static final String DISPLAY_NAME = "Tool Behaviour";
    private final String name;

    AutomaticRefillingToolBehaviours(String name) {
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
        return Tooltips.AR_TOOL_BEHAVIOUR;
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

