package net.origins.inventive_inventory.features.locked_slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.util.ScreenCheck;

import java.util.ArrayList;
import java.util.List;

public class LockedSlots extends ArrayList<Integer> {

    public LockedSlots(List<Integer> lockedSlots) {
        super(lockedSlots);
    }

    public LockedSlots adjust() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        if (!ScreenCheck.isPlayerHandler()) {
            this.replaceAll(slot -> slot + (screenHandler.slots.size() - PlayerInventory.MAIN_SIZE) - PlayerScreenHandler.EQUIPMENT_END);
        }
        return this;
    }
}
