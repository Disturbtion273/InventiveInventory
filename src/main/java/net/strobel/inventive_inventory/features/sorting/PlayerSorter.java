package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

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
        inventory = combineInventoryStacks(inventory, syncId);
        inventory = alignStacks(inventory, syncId);
        inventory = sort(inventory, syncId);
        inventory = sortByCount(inventory, syncId);
    }

    private static Inventory combineInventoryStacks(Inventory inventory, int syncId) {
        for (int i = inventory.size() - 5; i > 8; i--) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty() || stack.getCount() == stack.getMaxCount()) continue;
            for (int j = i - 1; j > 8; j--) {
                ItemStack tempStack = inventory.getStack(j);
                if (tempStack.isEmpty() || tempStack.getCount() == tempStack.getMaxCount()) continue;
                if (ItemStack.areItemsEqual(stack, tempStack)) {
                    if (stack.getCount() + tempStack.getCount() > stack.getMaxCount()) {
                        combineStack(i, j, true, syncId);
                        break;
                    } else {
                        combineStack(i, j, false, syncId);
                    }
                }
            }

        }
        return Sorter.getPlayerInventory();
    }

    public static Inventory alignStacks(Inventory inventory, int syncId) {
        int lastFullSlot;
        for (int i = 9; i < inventory.size() - 5; i++) {
            if (!inventory.getStack(i).isEmpty()) {
                lastFullSlot = i;
                Sorter.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, Sorter.player);
                for (int j = 9; j < lastFullSlot; j++) {
                    if (inventory.getStack(j).isEmpty()) {
                        Sorter.interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, Sorter.player);
                    }
                }
                Sorter.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, Sorter.player);
            }
        }
        return Sorter.getPlayerInventory();
    }

    private static Inventory sort(Inventory inventory, int syncId) {
        for (int i = 9; i < inventory.size() - 5; i++) {
            for (int j = i + 1; j < inventory.size() - 5; j++) {
                if (!inventory.getStack(i).isEmpty() && !inventory.getStack(j).isEmpty()) {
                    int nameComparison = inventory.getStack(i).getName().getString().compareTo(inventory.getStack(j).getName().getString());
                    if (nameComparison > 0) {
                        swapItems(i, j, syncId);
                    }
                }
            }
        }
        return Sorter.getPlayerInventory();
    }

    private static Inventory sortByCount(Inventory inventory, int syncId) {
        for (int i = inventory.size() - 5; i > 8; i--) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty() || stack.getCount() != stack.getMaxCount()) continue;
            for (int j = i - 1; j > 8; j--) {
                ItemStack tempStack = inventory.getStack(j);
                if (tempStack.isEmpty() || tempStack.getCount() == tempStack.getMaxCount()) continue;
                if (ItemStack.areItemsEqual(stack, tempStack)) {
                    swapItems(i, j, syncId);
                }
            }
        }
        return Sorter.getPlayerInventory();
    }


    public static void swapItems(int slot, int swapWith, int syncId) {
        Sorter.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, Sorter.player);
        Sorter.interactionManager.clickSlot(syncId, swapWith, 0, SlotActionType.PICKUP, Sorter.player);
        Sorter.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, Sorter.player);
    }

    public static void combineStack(int slot, int combineWith, boolean isTooMuch, int syncId) {
        Sorter.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, Sorter.player);
        Sorter.interactionManager.clickSlot(syncId, combineWith, 0, SlotActionType.PICKUP, Sorter.player);
        if (isTooMuch) {
            Sorter.player.sendMessage(Text.of("Is Too Much"));
            Sorter.interactionManager.clickSlot(syncId, combineWith, 0, SlotActionType.PICKUP, Sorter.player);
        }
    }
}
