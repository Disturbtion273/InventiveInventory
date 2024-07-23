package net.origins.inventive_inventory.config.enums;

import net.minecraft.text.Style;
import net.minecraft.text.Text;

public interface Configurable {
    String getName();
    String getDisplayName();
    Text getTooltip();
    default Style getStyle() {
        return Style.EMPTY;
    }
}
