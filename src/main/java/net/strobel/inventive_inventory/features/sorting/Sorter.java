package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.handler.InteractionHandler;
import net.strobel.inventive_inventory.slots.InventorySlots;

import java.util.Comparator;
import java.util.List;

public class Sorter {

    public static boolean cursorCleared(InventorySlots inventorySlots, ScreenHandler screenHandler) {
        if (!InteractionHandler.hasEmptyCursor()) {
            clearCursor(inventorySlots, screenHandler);
        }
        return InteractionHandler.hasEmptyCursor();
    }

    public static void mergeItemStacks(InventorySlots inventorySlots, ScreenHandler screenHandler) {
        for (int slot : inventorySlots) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
                InteractionHandler.clickStack(slot);
                for (int tempSlot = slot + 1; InteractionHandler.getCursorStack().getCount() < InteractionHandler.getCursorStack().getMaxCount()
                        && tempSlot <= inventorySlots.getLastSlot() && !InteractionHandler.getCursorStack().isEmpty(); tempSlot++) {
                    if (LockedSlotsHandler.getLockedSlots().adjust().contains(tempSlot)) continue;
                    if (ItemStack.areItemsEqual(InteractionHandler.getCursorStack(), screenHandler.getSlot(tempSlot).getStack())) {
                        InteractionHandler.clickStack(tempSlot);
                    }
                }
                if (!InteractionHandler.hasEmptyCursor()) {
                    InteractionHandler.clickStack(slot);
                }
            }
        }
    }

    public static void sortItemStacks(InventorySlots inventorySlots, ScreenHandler screenHandler) {
        List<Integer> sortedSlots = getSortedSlots(inventorySlots, screenHandler);

        for (int i = 0; i < sortedSlots.size(); i++) {
            InteractionHandler.swapStacks(sortedSlots.get(i), inventorySlots.get(i));
            sortedSlots = getSortedSlots(inventorySlots, screenHandler);
        }
    }

    private static List<Integer> getSortedSlots(InventorySlots inventorySlots, ScreenHandler screenHandler) {
        return inventorySlots.stream()
                .filter(slot -> !screenHandler.getSlot(slot).getStack().isEmpty())
                .sorted(Comparator.comparing((Integer slot) -> screenHandler.getSlot(slot).getStack().getName().getString())
                        .thenComparing(slot -> screenHandler.getSlot(slot).getStack().getCount(), Comparator.reverseOrder()))
                .toList();
    }

    private static void clearCursor(InventorySlots inventorySlots, ScreenHandler screenHandler) {
        for (int slot : inventorySlots) {
            if (ItemStack.areItemsEqual(screenHandler.getSlot(slot).getStack(), InteractionHandler.getCursorStack())) {
                if (!InteractionHandler.hasEmptyCursor()) {
                    InteractionHandler.clickStack(slot);
                }
            }
        }

        for (int slot : inventorySlots) {
            if (screenHandler.getSlot(slot).getStack().isEmpty()) {
                if (!InteractionHandler.hasEmptyCursor()) {
                    InteractionHandler.clickStack(slot);
                }
            }
        }
    }
}
