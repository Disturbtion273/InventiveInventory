package net.origins.inventive_inventory.util.mouse;

import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.util.ScreenCheck;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotTypes;

public class MouseLocation {
    private static Slot hoveredSlot;

    public static void setHoveredSlot(Slot slot) {
        hoveredSlot = slot;
    }

    public static Slot getHoveredSlot() {
        return hoveredSlot;
    }

    public static boolean isOverInventory() {
        if (ScreenCheck.isPlayerInventory()) return true;
        else if (hoveredSlot != null) return PlayerSlots.get().append(SlotTypes.HOTBAR).contains(hoveredSlot.id);
        else return false;
    }
}
