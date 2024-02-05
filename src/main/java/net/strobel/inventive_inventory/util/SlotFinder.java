package net.strobel.inventive_inventory.util;

import net.minecraft.screen.slot.Slot;
import net.strobel.inventive_inventory.InventiveInventory;

public class SlotFinder {
    public static Slot getSlotAtPosition(int x, int y) {
        for (Slot slot : InventiveInventory.getScreenHandler().slots) {
            if (isPointInRegion(slot.x, slot.y, x, y)) {
                return slot;
            }
        }
        return null;
    }

    private static boolean isPointInRegion(int rectX, int rectY, int pointX, int pointY) {
        return pointX >= rectX - 1 && pointX < rectX + 16 + 1 && pointY >= rectY - 1 && pointY < rectY + 16 + 1;
    }

    public static Slot getSlotFromSlotIndex(int slotIndex) {
        return InventiveInventory.getScreenHandler().getSlot(slotIndex);
    }
}
