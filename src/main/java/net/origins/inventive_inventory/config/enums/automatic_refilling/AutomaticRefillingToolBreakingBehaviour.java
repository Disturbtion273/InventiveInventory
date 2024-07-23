package net.origins.inventive_inventory.config.enums.automatic_refilling;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.accessibility.Tooltips;
import net.origins.inventive_inventory.config.enums.Configurable;

public enum AutomaticRefillingToolBreakingBehaviour implements Configurable{
    KEEP_TOOL("Keep Tool"),
    BREAK_TOOL("Break Tool");

    public static final String CONFIG_KEY = "Automatic Refilling Tool Breaking Behaviour";
    public static final String DISPLAY_NAME = "Tool Breaking Behaviour";
    private final String name;

    AutomaticRefillingToolBreakingBehaviour(String name) {
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
        return Tooltips.AR_TOOL_BREAKING_BEHAVIOUR;
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
