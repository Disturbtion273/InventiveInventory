package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

public class ContainerSorter {
    public static void sortContainer(int syncId) {
        Inventory inventory = Sorter.getContainerInventory();
        inventory = combineInventoryStacks(inventory, syncId);
        inventory = alignStacks(inventory, syncId);

    }

    private static Inventory combineInventoryStacks(Inventory inventory, int syncId) {
        for (int i = inventory.size() - 1; i >= 0; i--) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty() || stack.getCount() == stack.getMaxCount()) continue;
            for (int j = i - 1; j >= 0; j--) {
                ItemStack tempStack = inventory.getStack(j);
                if (tempStack.isEmpty() || tempStack.getCount() == tempStack.getMaxCount()) continue;
                if (ItemStack.areItemsEqual(stack, tempStack)) {
                    if (stack.getCount() + tempStack.getCount() > stack.getMaxCount()) {
                        Sorter.combineStack(i, j, true, syncId);
                        break;
                    } else {
                        Sorter.combineStack(i, j, false, syncId);
                    }
                }
            }

        }
        return Sorter.getContainerInventory();
    }

    public static Inventory alignStacks(Inventory inventory, int syncId) {
        int lastFullSlot;
        for (int i = 0; i < inventory.size(); i++) {
            if (!inventory.getStack(i).isEmpty()) {
                lastFullSlot = i;
                Sorter.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, Sorter.player);
                for (int j = 0; j < lastFullSlot; j++) {
                    if (inventory.getStack(j).isEmpty()) {
                        Sorter.interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, Sorter.player);
                    }
                }
                Sorter.interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, Sorter.player);
            }
        }
        return Sorter.getContainerInventory();
    }
}
