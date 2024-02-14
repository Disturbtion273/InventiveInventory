package net.strobel.inventive_inventory.util;

import net.strobel.inventive_inventory.slots.PlayerSlots;

public class MousePosition {
    private static Integer slot;

    public static void setHoveredSlot(int slot) {
        MousePosition.slot = slot;
    }

    public static int getHoveredSlot() { return slot;}

    public static boolean isOverInventory() {
        if (ScreenCheck.isRegularInventory() || ScreenCheck.isCreativeInventory()) {
            return true;
        } else {
            return PlayerSlots.getWithHotbar().contains(slot);
        }
    }
}
