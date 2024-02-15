package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.slots.ContainerSlots;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.MousePosition;


public class SortingHandler {

    public static void sort() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

        if (MousePosition.isOverInventory()) {
            sortPlayerInventory(screenHandler);
        } else {
            sortContainerInventory(screenHandler);
        }
    }

    private static void sortPlayerInventory(ScreenHandler screenHandler) {
        if (Sorter.cursorCleared(PlayerSlots.getWithHotbar().excludeLockedSlots(), screenHandler)) {
            Sorter.mergeItemStacks(PlayerSlots.get().excludeOffhand().excludeLockedSlots(), screenHandler);
            Sorter.sortItemStacks(PlayerSlots.get().excludeOffhand().excludeLockedSlots(), screenHandler);
        } else {
            sendWarning();
        }
    }

    private static void sortContainerInventory(ScreenHandler screenHandler) {
        if (Sorter.cursorCleared(ContainerSlots.get(), screenHandler)) {
            Sorter.mergeItemStacks(ContainerSlots.get(), screenHandler);
            Sorter.sortItemStacks(ContainerSlots.get(), screenHandler);
        } else {
            sendWarning();
        }
    }

    private static void sendWarning() {
        Text text = Text.of("No operating room available. Try emptying the cursor.")
                .copy().setStyle(Style.EMPTY.withColor(Formatting.RED));
        InventiveInventory.getPlayer().sendMessage(text, true);
    }
}
