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
        SlotRange slotRange = MouseLocation.isOverInventory() ? PlayerSlots.get() : ContainerSlots.get(); // BEI PLAYERSLOTS NOCH LOCKED SLOTS EXCLUDEN


        Sorter.mergeItemStacks(slotRange, screenHandler);

//        if (Sorter.cursorCleared(PlayerSlots + Container, screenHandler) ) {
//            SlotRange slotRange = ContainerSlots.get();
//            if (MouseLocation.isOverInventory()) {
//                slotRange = Playerslots.get().exlcude(LockedSlots);
//            }
//            Sorter.mergeItemStacks(slotRange, screenHandler);
//            Sorter.sortItemStacks(slotRange, screenHandler);
//        } else sendWarning();
    }
}
