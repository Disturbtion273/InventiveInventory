package net.strobel.inventive_inventory.slots;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventory;

public class PlayerSlots {

    public static InventorySlots get(boolean withHotbar) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

        int from, to;
        if (screenHandler instanceof PlayerScreenHandler || screenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            from = PlayerScreenHandler.INVENTORY_START;
            if (withHotbar) {
                to = PlayerScreenHandler.HOTBAR_END;
                return new InventorySlots(from, to, PlayerScreenHandler.OFFHAND_ID);
            } else {
                to = PlayerScreenHandler.INVENTORY_END;
                return new InventorySlots(from, to);
            }
        } else {
            from = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE;
            to = withHotbar ? screenHandler.slots.size() : screenHandler.slots.size() - PlayerInventory.getHotbarSize();
            return new InventorySlots(from, to);
        }
    }
}
