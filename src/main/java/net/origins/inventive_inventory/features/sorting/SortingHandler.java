package net.origins.inventive_inventory.features.sorting;

import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.util.mouse.MouseLocation;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.awt.*;

public class SortingHandler {
    public static void sort() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

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
