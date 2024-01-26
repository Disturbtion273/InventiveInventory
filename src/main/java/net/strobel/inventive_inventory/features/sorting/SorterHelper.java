package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.enums.PlayerInventorySlots;
import net.strobel.inventive_inventory.handler.InteractionHandler;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class SorterHelper {

    public static boolean cursorCleared(PlayerInventorySlots inventorySlots, ScreenHandler screenHandler) {
        if (!InteractionHandler.hasEmptyCursor()) {
            clearCursor(inventorySlots, screenHandler);
        }
        return InteractionHandler.hasEmptyCursor();
    }

    public static void mergeItemStacks(PlayerInventorySlots inventorySlots, ScreenHandler screenHandler) {
        for (int slot : inventorySlots.getSlots()) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
                InteractionHandler.clickStack(slot);
                for (int tempSlot = slot + 1; InteractionHandler.getCursorStack().getCount() < InteractionHandler.getCursorStack().getMaxCount()
                        && tempSlot <= inventorySlots.getLastSlot(); tempSlot++) {
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

    public static void sortItemStacks(PlayerInventorySlots inventorySlots, ScreenHandler screenHandler) {
        List<Integer> sortedSlots = getSortedSlots(inventorySlots, screenHandler);

        for (int i = 0; i < sortedSlots.size(); i++) {
            InteractionHandler.swapStacks(sortedSlots.get(i), i + inventorySlots.getFirstSlot());
            sortedSlots = getSortedSlots(inventorySlots, screenHandler);
        }
    }

    private static List<Integer> getSortedSlots(PlayerInventorySlots inventorySlots, ScreenHandler screenHandler) {
        return IntStream.of(inventorySlots.getSlots())
                .boxed()
                .filter(slot -> !screenHandler.getSlot(slot).getStack().isEmpty())
                .sorted(Comparator.comparing((Integer slot) -> screenHandler.getSlot(slot).getStack().getName().getString())
                        .thenComparing(slot -> screenHandler.getSlot(slot).getStack().getCount(), Comparator.reverseOrder()))
                .toList();
    }

    private static void clearCursor(PlayerInventorySlots inventorySlots, ScreenHandler screenHandler) {

        for (int slot : inventorySlots.getSlots()) {
            if (ItemStack.areItemsEqual(screenHandler.getSlot(slot).getStack(), InteractionHandler.getCursorStack())) {
                if (!InteractionHandler.hasEmptyCursor()) {
                    InteractionHandler.clickStack(slot);
                }
            }
        }

        for (int slot : inventorySlots.getSlots()) {
            if (screenHandler.getSlot(slot).getStack().isEmpty()) {
                if (!InteractionHandler.hasEmptyCursor()) {
                    InteractionHandler.clickStack(slot);
                }
            }
        }
    }
}
