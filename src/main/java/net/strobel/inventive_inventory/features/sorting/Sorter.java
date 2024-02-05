package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.slots.ContainerSlots;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.MousePosition;


public class Sorter {

    public static void sort() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

        if (MousePosition.isOverInventory(screenHandler)) {
            sortPlayerInventory(screenHandler);
        } else {
            sortContainerInventory(screenHandler);
        }
    }

    private static void sortPlayerInventory(ScreenHandler screenHandler) {
        if (SorterHelper.cursorCleared(PlayerSlots.get(true).excludeLockedSlots(), screenHandler)) {
            SorterHelper.mergeItemStacks(PlayerSlots.get(false).excludeLockedSlots(), screenHandler);
            SorterHelper.sortItemStacks(PlayerSlots.get(false).excludeLockedSlots(), screenHandler);
        } else {
            InventiveInventory.getPlayer().sendMessage(
                    Text.of("No operating room available. Try emptying the cursor."));
        }
    }

    private static void sortContainerInventory(ScreenHandler screenHandler) {
        if (SorterHelper.cursorCleared(ContainerSlots.getInventory(), screenHandler)) {
            SorterHelper.mergeItemStacks(ContainerSlots.getInventory(), screenHandler);
            SorterHelper.sortItemStacks(ContainerSlots.getInventory(), screenHandler);
        } else {
            InventiveInventory.getPlayer().sendMessage(
                    Text.of("No operating room available. Try emptying the cursor."));
        }
    }
}
