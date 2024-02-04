package net.strobel.inventive_inventory.features.locked_slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventoryClient;

import java.util.List;
import java.util.ArrayList;

public class LockedSlots extends ArrayList<Integer> {

    public LockedSlots(List<Integer> lockedSlots) {
        super(lockedSlots);
    }

    public LockedSlots adjust() {
        ScreenHandler screenHandler = InventiveInventoryClient.getScreenHandler();
        if (!(screenHandler instanceof PlayerScreenHandler)) {
            this.replaceAll(integer -> integer + (screenHandler.slots.size() - PlayerInventory.MAIN_SIZE) - 9);
        }
        return this;
    }

    public void toggle(int slot) {
        if (this.contains(slot)) {
            this.remove(Integer.valueOf(slot));
        } else {
            this.add(slot);
        }
    }
}
