package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.slots.ContainerSlots;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.MousePosition;


public class Sorter {

    public static void sort() {
        ScreenHandler screenHandler = InventiveInventoryClient.getScreenHandler();

        if (MousePosition.isOverInventory(screenHandler)) {
            sortPlayerInventory(screenHandler);
        } else {
            sortContainerInventory(screenHandler);
        }
    }

    private static void sortPlayerInventory(ScreenHandler screenHandler) {
        if (SorterHelper.cursorCleared(PlayerSlots.getFullInventoryLockedSlotsExcluded(), screenHandler)) {
            SorterHelper.mergeItemStacks(PlayerSlots.getUpperInventoryLockedSlotsExcluded(), screenHandler);
            SorterHelper.sortItemStacks(PlayerSlots.getUpperInventoryLockedSlotsExcluded(), screenHandler);
        } else {
            InventiveInventoryClient.getPlayer().sendMessage(
                    Text.of("No operating room available. Try emptying the cursor."));
        }
    }

    private static void sortContainerInventory(ScreenHandler screenHandler) {
        if (SorterHelper.cursorCleared(ContainerSlots.getInventory(), screenHandler)) {
            SorterHelper.mergeItemStacks(ContainerSlots.getInventory(), screenHandler);
            SorterHelper.sortItemStacks(ContainerSlots.getInventory(), screenHandler);
        } else {
            InventiveInventoryClient.getPlayer().sendMessage(
                    Text.of("No operating room available. Try emptying the cursor."));
        }
    }
}
