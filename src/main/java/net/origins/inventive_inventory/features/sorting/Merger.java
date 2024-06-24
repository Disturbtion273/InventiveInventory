package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.SlotRange;


public class Merger {

    public static void mergeItemStacks(SlotRange slotRange) {
        generalMerge(slotRange);
        mergeCursorStack(slotRange);
    }

    private static void generalMerge(SlotRange slotRange) {
        for (int slot : slotRange) {
            ItemStack stack = InteractionHandler.getStackFromSlot(slot);
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
                InteractionHandler.leftClickStack(slot);
                for (int tempSlot = slot + 1; InteractionHandler.getCursorStack().getCount() < InteractionHandler.getCursorStack().getMaxCount()
                        && tempSlot <= slotRange.getLast() && !InteractionHandler.getCursorStack().isEmpty(); tempSlot++) {
                    if (ItemStack.areItemsEqual(InteractionHandler.getCursorStack(), InteractionHandler.getStackFromSlot(tempSlot))) {
                        InteractionHandler.leftClickStack(tempSlot);
                    }
                }
                InteractionHandler.leftClickStack(slot);
            }
        }
    }

    private static void mergeCursorStack(SlotRange slotRange) {
        Integer emptySlot = null;

        for (int slot : slotRange) {
            ItemStack stack = InteractionHandler.getStackFromSlot(slot);
            if (ItemStack.areItemsEqual(InteractionHandler.getCursorStack(), stack)) {
                InteractionHandler.leftClickStack(slot);
            } else if (stack.isEmpty() && emptySlot == null) {
                emptySlot = slot;
            }
        }
        if (!InteractionHandler.isCursorEmpty() && emptySlot != null) InteractionHandler.leftClickStack(emptySlot);
    }
}
