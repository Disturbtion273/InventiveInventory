package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.SlotRange;

public class Sorter {

    public static void mergeItemStacks(SlotRange slotRange, ScreenHandler screenHandler) {
        ItemStack cursorStack = screenHandler.getCursorStack();
        for (int slot : slotRange) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
                InteractionHandler.clickStack(slot);
                for (int tempSlot = slot + 1; )
            }
        }
    }

//    public static void mergeItemStacksOLD(SlotRange slotRange, ScreenHandler screenHandler) {
//        for (int slot : slotRange) {
//            ItemStack stack = screenHandler.getSlot(slot).getStack();
//            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
//                InteractionHandler.clickStack(slot);
//                for (int tempSlot = slot + 1; InteractionHandler.getCursorStack().getCount() < InteractionHandler.getCursorStack().getMaxCount()
//                        && tempSlot <= slotRange.getLastSlot() && !InteractionHandler.getCursorStack().isEmpty(); tempSlot++) {
//                    if (LockedSlotsHandler.getLockedSlots().adjust().contains(tempSlot)) continue;
//                    if (ItemStack.areItemsEqual(InteractionHandler.getCursorStack(), screenHandler.getSlot(tempSlot).getStack())) {
//                        InteractionHandler.clickStack(tempSlot);
//                    }
//                }
//                if (!InteractionHandler.hasEmptyCursor()) {
//                    InteractionHandler.clickStack(slot);
//                }
//            }
//        }
//    }
}
