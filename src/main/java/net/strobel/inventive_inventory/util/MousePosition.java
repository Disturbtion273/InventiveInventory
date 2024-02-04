package net.strobel.inventive_inventory.util;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.slots.PlayerSlots;

public class MousePosition {
    private static Integer slot;

    public static void setHoveredSlot(int slot) {
        MousePosition.slot = slot;
    }

    public static int getHoveredSlot() { return slot;}

    public static boolean isOverInventory(ScreenHandler screenHandler) {
        if (screenHandler instanceof PlayerScreenHandler || screenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            return true;
        } else {
            return PlayerSlots.get(true).contains(slot);
        }
    }
}
