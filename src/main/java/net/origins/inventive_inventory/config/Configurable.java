package net.origins.inventive_inventory.config;

import net.minecraft.text.Style;

public interface Configurable {
    String getName();
    default Style getStyle() {
        return Style.EMPTY;
    }
    void toggle();
}
