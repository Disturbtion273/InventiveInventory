package net.origins.inventive_inventory.config.enums.profiles;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.accessibility.Tooltips;
import net.origins.inventive_inventory.config.enums.Configurable;

public enum ProfilesLockedSlotsBehaviours implements Configurable {
    IGNORE("Ignore"),
    INCLUDE("Include");

    public static final String CONFIG_KEY = "Profile Locked Slots Behaviour";
    public static final String DISPLAY_NAME = "Locked Slots Behaviour";
    private final String name;

    ProfilesLockedSlotsBehaviours(String name) {
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
        return Tooltips.LOCKED_SLOTS_BEHAVIOUR;
    }

    @Override
    public void toggle() {
        ConfigManager.P_LS_BEHAVIOUR = values()[(this.ordinal() + 1) % values().length];
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
