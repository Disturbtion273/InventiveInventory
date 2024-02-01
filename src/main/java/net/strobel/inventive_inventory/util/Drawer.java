package net.strobel.inventive_inventory.util;

import net.minecraft.client.gui.DrawContext;

public class Drawer {
    public static void drawSlotBorder(DrawContext context, int x, int y, int color) {
        int width = 15;
        int height = 15;
        context.fill(x, y, x + width, y + 1, color);
        context.fill(x, y + height, x + width + 1, y + height + 1, color);
        context.fill(x, y, x + 1, y + height, color);
        context.fill(x + width, y, x + width + 1, y + height, color);
    }
}
