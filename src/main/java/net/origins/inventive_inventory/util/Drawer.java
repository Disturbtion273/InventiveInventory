package net.origins.inventive_inventory.util;

import net.minecraft.client.gui.DrawContext;

public class Drawer {

    public static void drawSlotBackground(DrawContext context, int x, int y, int color, int z) {
        int width = 16, height = 16;
        context.fillGradient(x, y, x + width, y + height, z, color, color);
    }
}
