package net.strobel.inventive_inventory.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventoryClient;

public class ContainerSlots {
    public static final PlayerInventory playerInventory = InventiveInventoryClient.getPlayer().getInventory();
    public static InventorySlots getInventory() {
        ScreenHandler screenHandler = InventiveInventoryClient.getScreenHandler();
        int from = 0;
        int to = screenHandler.slots.size() - playerInventory.main.size();
        return new InventorySlots(from, to);
    }
}
