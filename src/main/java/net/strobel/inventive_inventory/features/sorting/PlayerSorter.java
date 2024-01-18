package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;

public class PlayerSorter {
    public static void sortInventory(int syncId) {
        if (Sorter.player.isCreative()) {
            if (!Sorter.player.playerScreenHandler.getCursorStack().isEmpty()) {
                for (int i = 9; i < Sorter.getPlayerInventory().size() - 5; i++) {
                    if (Sorter.getPlayerInventory().getStack(i).isEmpty()) {
                        Sorter.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, Sorter.player);
                        break;
                    }
                }
            }
        }
        Inventory inventory = Sorter.getPlayerInventory();
        inventory = combineInventoryStacks(inventory, true, syncId);
//        inventory = alignStacks(inventory, syncId);
//        inventory = sort(inventory, syncId);
//        inventory = sortByCount(inventory, syncId);
    }

    private static Inventory combineInventoryStacks(Inventory inventory, boolean isPlayerInventory, int syncId) {
        ScreenHandler handler = Sorter.player.currentScreenHandler;
        int until = isPlayerInventory ? 8: 0;
        int armor = isPlayerInventory ? 6: 0;

        if (!handler.getCursorStack().isEmpty()) {
            int j = inventory.size() - armor;
            while (!handler.getCursorStack().isEmpty() && j > until) {
                ItemStack tempStack = inventory.getStack(j);
                if (tempStack.isEmpty()) {
                    Sorter.interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, Sorter.player);
                    break;
                }

                if (tempStack.getCount() == tempStack.getMaxCount() || !ItemStack.areItemsEqual(handler.getCursorStack(), tempStack)) {
                    j--;
                    continue;
                }
                Sorter.interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, Sorter.player);
                j--;
            }
        }

        for (int i = inventory.size() - armor; i > until; i--) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty() || stack.getCount() == stack.getMaxCount()) continue;
            Sorter.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, Sorter.player);
            int j = i - 1;
            while (!handler.getCursorStack().isEmpty() && j > until) {
                ItemStack tempStack = inventory.getStack(j);
                if (tempStack.isEmpty() || tempStack.getCount() == tempStack.getMaxCount() || !ItemStack.areItemsEqual(handler.getCursorStack(), tempStack)) {
                    j--;
                    continue;
                }
                Sorter.interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, Sorter.player);
                j--;
            }
            if (!handler.getCursorStack().isEmpty()) {
                Sorter.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, Sorter.player);
            }
        }
        return Sorter.getPlayerInventory();
    }
}