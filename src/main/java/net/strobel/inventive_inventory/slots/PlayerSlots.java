package net.strobel.inventive_inventory.slots;

import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.util.Utilities;

public class PlayerSlots {

    public static InventorySlots get(boolean withHotbar) {
        ScreenHandler screenHandler = InventiveInventoryClient.getScreenHandler();
        return Utilities.calcPlayerInventorySlots(screenHandler, withHotbar);
    }
}
