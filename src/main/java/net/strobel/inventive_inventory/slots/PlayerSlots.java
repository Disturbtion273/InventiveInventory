package net.strobel.inventive_inventory.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.util.ScreenCheck;

public class PlayerSlots {

    public static InventorySlots getWithHotbar() {
        int from, to;
        if (ScreenCheck.isRegularInventory()) {
            from = PlayerScreenHandler.INVENTORY_START;
            to = PlayerScreenHandler.HOTBAR_END;
            return new InventorySlots(from, to, PlayerScreenHandler.OFFHAND_ID);
        } else {
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            from = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE;
            to = screenHandler.slots.size();
            return new InventorySlots(from, to);
        }
    }

    public static InventorySlots get() {
        int from, to;
        if (ScreenCheck.isRegularInventory()) {
            from = PlayerScreenHandler.INVENTORY_START;
            to = PlayerScreenHandler.INVENTORY_END;
            return new InventorySlots(from, to, PlayerScreenHandler.OFFHAND_ID);
        } else {
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            from = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE;
            to = screenHandler.slots.size() - PlayerInventory.getHotbarSize();
            return new InventorySlots(from, to);
        }
    }

    public static InventorySlots getWithHotbarAndArmor() {
        int from, to;
        from = PlayerScreenHandler.EQUIPMENT_START;
        to = PlayerScreenHandler.HOTBAR_END;
        return new InventorySlots(from, to, PlayerScreenHandler.OFFHAND_ID);
    }

    public static InventorySlots getHotbarAndEquipment() {
        int from = PlayerScreenHandler.EQUIPMENT_START;
        int to = PlayerScreenHandler.EQUIPMENT_END;
        int from2 = PlayerScreenHandler.HOTBAR_START;
        int to2 = PlayerScreenHandler.OFFHAND_ID;
        return new InventorySlots(from, to, from2, to2);
    }

    public static InventorySlots getHotbar() {
        int from = PlayerScreenHandler.HOTBAR_START;
        int to = PlayerScreenHandler.HOTBAR_END;
        return new InventorySlots(from, to);
    }

    public static InventorySlots getInventory() {
        int from = PlayerScreenHandler.INVENTORY_START;
        int to = PlayerScreenHandler.INVENTORY_END;
        return new InventorySlots(from, to);
    }
}
