package net.origins.inventive_inventory.features.sorting;

import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.util.mouse.MouseLocation;
import net.origins.inventive_inventory.util.slots.ContainerSlots;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;

public class SortingHandler {
    public static void sort() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        SlotRange slotRange = MouseLocation.isOverInventory() ? PlayerSlots.get() : ContainerSlots.get(); // TODO: Locked Slots have to be excluded

        Sorter.mergeItemStacks(slotRange, screenHandler);
        // TODO: Sorting Algorithm
    }
}
