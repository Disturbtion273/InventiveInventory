package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.mouse.MouseLocation;
import net.origins.inventive_inventory.util.slots.ContainerSlots;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;

public class SortingHandler {
    public static void sort() {
        SlotRange slotRange = MouseLocation.isOverInventory() ? PlayerSlots.get() : ContainerSlots.get(); // TODO: Locked Slots have to be excluded
        ItemStack targetStack = InteractionHandler.getCursorStack().copy();

        Merger.mergeItemStacks(slotRange);
        Sorter.sortItemStacks(slotRange, targetStack);
    }
}
