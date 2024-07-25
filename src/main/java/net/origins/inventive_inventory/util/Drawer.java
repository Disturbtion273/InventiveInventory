package net.origins.inventive_inventory.util;

import net.minecraft.client.gui.DrawContext;

public class Drawer {

    public static void drawSlotBackground(DrawContext context, int x, int y, int color, int z) {
        int width = 16, height = 16;
        context.fillGradient(x, y, x + width, y + height, z, color, color);
    }
    
    public static void drawProfileHotbar(DrawContext context, int x, int y) {
        context.drawTexture(Textures.HOTBAR, x, y, 0, 0, 0, 205, 20, 205, 20);
        context.fill(x + 2, y + 2, x + 2 + 16, y + 2 + 16, -2130706433);
        x = x + 27;
        y = y + 2;
        for (int i = 0; i < 9; i++) {
            context.fill(x, y, x + 16, y + 16, -2130706433);
            x += 20;
        }
    }
}
