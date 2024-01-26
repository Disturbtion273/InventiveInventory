package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.enums.PlayerInventorySlots;

public class Sorter2 {
    public static void sortPlayerInventory() {
        ScreenHandler screenHandler = InventiveInventoryClient.getScreenHandler();

        if (SorterHelper.cursorCleared(PlayerInventorySlots.FULL_INVENTORY.getSlots(), screenHandler)) {
            SorterHelper.mergeItemStacks(screenHandler);
            SorterHelper.sortItemStacks(screenHandler);
        } else {
            InventiveInventoryClient.getPlayer().sendMessage(
                    Text.of("No operating room available. Try emptying the cursor."));
        }
    }
}
