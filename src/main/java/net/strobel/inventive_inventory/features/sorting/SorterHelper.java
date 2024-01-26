package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.enums.PlayerInventorySlots;
import net.strobel.inventive_inventory.handler.InteractionHandler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class SorterHelper {

    public static boolean cursorCleared(int[] inventorySlots, ScreenHandler screenHandler) {
        if (!InteractionHandler.hasEmptyCursor()) {
            clearCursor(inventorySlots, screenHandler);
        }
        return InteractionHandler.hasEmptyCursor();
    }

    public static void mergeItemStacks(ScreenHandler screenHandler) {
        for (int slot : PlayerInventorySlots.UPPER_INVENTORY.getSlots()) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
                InteractionHandler.clickStack(slot);
                for (int tempSlot = slot + 1; InteractionHandler.getCursorStack().getCount() < InteractionHandler.getCursorStack().getMaxCount()
                        && tempSlot <= PlayerInventorySlots.UPPER_INVENTORY.getLastSlot(); tempSlot++) {
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

    public static void sortItemStacks(ScreenHandler screenHandler) {
        List<Integer> sortedSlots = getSortedSlots(screenHandler);

        for (int i = 0; i < sortedSlots.size(); i++) {
            InteractionHandler.swapStacks(sortedSlots.get(i), i + PlayerInventorySlots.UPPER_INVENTORY.getFirstSlot());
            sortedSlots = getSortedSlots(screenHandler);
        }
    }

    private static List<Integer> getSortedSlots(ScreenHandler screenHandler) {
        return IntStream.of(PlayerInventorySlots.UPPER_INVENTORY.getSlots())
                .boxed()
                .filter(slot -> !screenHandler.getSlot(slot).getStack().isEmpty())
                .sorted(Comparator.comparing((Integer slot) -> screenHandler.getSlot(slot).getStack().getName().getString())
                        .thenComparing(slot -> screenHandler.getSlot(slot).getStack().getCount(), Comparator.reverseOrder()))
                .toList();
    }

    private static void clearCursor(int[] inventorySlots, ScreenHandler screenHandler) {

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
