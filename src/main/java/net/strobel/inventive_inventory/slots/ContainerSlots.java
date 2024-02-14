package net.strobel.inventive_inventory.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventory;

public class ContainerSlots {
    public static InventorySlots get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        int from = 0;
        int to = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE;
        return new InventorySlots(from, to);
    }
}
