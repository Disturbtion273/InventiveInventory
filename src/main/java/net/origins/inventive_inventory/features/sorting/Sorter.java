package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.SlotRange;

public class Sorter {
    public static boolean cursorCleared(SlotRange slotRange, ScreenHandler screenHandler) {
        if (!InteractionHandler.isCursorEmpty()) {
            clearCursor(slotRange, screenHandler);
        }
        return InteractionHandler.isCursorEmpty();
    }

    private static void clearCursor(SlotRange slotRange, ScreenHandler screenHandler) {
        for (int slot : slotRange) {
            if (ItemStack.areItemsEqual(screenHandler.getSlot(slot).getStack(), InteractionHandler.getCursorStack()) || screenHandler.getSlot(slot).getStack().isEmpty()) {
                if (!InteractionHandler.isCursorEmpty()) InteractionHandler.clickStack(slot);
                else break;
            }
        }
    }
}
