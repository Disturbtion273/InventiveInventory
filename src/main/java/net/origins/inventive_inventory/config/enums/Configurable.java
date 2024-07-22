package net.origins.inventive_inventory.config.enums;

import net.minecraft.text.Style;

public interface Configurable {
    String getName();
    String getDisplayName();
    default Style getStyle() {
        return Style.EMPTY;
    }
    void toggle();
}
