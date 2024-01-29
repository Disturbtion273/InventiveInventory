package net.strobel.inventive_inventory.util;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.slots.PlayerSlots;

import java.util.Arrays;

public class MousePosition {
    private static Integer slot;

    public static void setHoveredSlot(int slot) {
        MousePosition.slot = slot;
    }

    public static boolean isOverInventory(ScreenHandler screenHandler) {
        if (screenHandler instanceof PlayerScreenHandler || screenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            return true;
        } else {
            return Checker.isIncluded(slot, Arrays.stream(PlayerSlots.getFullInventory().getSlots()).boxed().toArray());
        }
    }
}
