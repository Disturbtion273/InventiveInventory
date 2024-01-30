package net.strobel.inventive_inventory.features.locked_slots;

import net.minecraft.text.Text;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.util.FileHandler;

public class LockedSlots {

    public static void save(int lockedSlot) {
        InventiveInventoryClient.getPlayer().sendMessage(Text.of("Saved"));

        //FileHandler.write(lockedSlots, "config/inventive_inventory/locked_slots.json");

    }
}
