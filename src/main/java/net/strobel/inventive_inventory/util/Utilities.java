package net.strobel.inventive_inventory.util;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.slots.InventorySlots;

import java.util.List;

public class Utilities {
    public static InventorySlots calcPlayerInventorySlots(ScreenHandler screenHandler, boolean withHotbar) {
        int from;
        int to;
        if (screenHandler instanceof PlayerScreenHandler || screenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            from = PlayerScreenHandler.INVENTORY_START;
            to = withHotbar ? PlayerScreenHandler.HOTBAR_END : PlayerScreenHandler.INVENTORY_END;
            return new InventorySlots(from, to, PlayerScreenHandler.OFFHAND_ID);
        } else {
            from = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE;
            to = withHotbar ? screenHandler.slots.size() : screenHandler.slots.size() - PlayerInventory.getHotbarSize();
            return new InventorySlots(from, to);
        }
    }

    public static List<Integer> adjustLockedSlots(List<Integer> lockedSlots, ScreenHandler screenHandler) {
        if (!(screenHandler instanceof PlayerScreenHandler)) {
            lockedSlots.replaceAll(integer -> integer + (screenHandler.slots.size() - PlayerInventory.MAIN_SIZE) - 9);
        }
        return lockedSlots;
    }
}
