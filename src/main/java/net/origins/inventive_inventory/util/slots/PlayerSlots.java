package net.origins.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;

public class PlayerSlots {
    private static final int HOTBAR_SIZE = 9;
    private static final int OFFHAND_SIZE = 1;

    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        int start = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE - (screenHandler instanceof PlayerScreenHandler ? OFFHAND_SIZE : 0);
        int stop = screenHandler.slots.size() - HOTBAR_SIZE - (screenHandler instanceof PlayerScreenHandler ? OFFHAND_SIZE : 0);
        return new SlotRange(start, stop);
    }

    public static SlotRange get(SlotTypes type) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        if (type == SlotTypes.HOTBAR) {
            int start = screenHandler.slots.size() - HOTBAR_SIZE - (screenHandler instanceof PlayerScreenHandler ? OFFHAND_SIZE : 0);
            int stop = screenHandler.slots.size() - (screenHandler instanceof PlayerScreenHandler ? OFFHAND_SIZE : 0);
            return new SlotRange(start, stop);
        } else if (type == SlotTypes.INVENTORY) {
            return PlayerSlots.get();
        } else throw new IllegalArgumentException("This SlotType is not valid in this function");
    }
}
