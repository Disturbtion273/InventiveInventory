package net.strobel.inventive_inventory.features.locked_slots;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;
import net.strobel.inventive_inventory.util.MousePosition;

import java.util.Arrays;

public class LockedSlots {

    private static final String LOCKED_SLOTS_PATH = InventiveInventoryClient.CONFIG_PATH + "locked_slots.json";
    public static void set() {
        JsonPrimitive slot = new JsonPrimitive(MousePosition.getHoveredSlot());
        JsonArray lockedSlots = FileHandler.get(LOCKED_SLOTS_PATH);
        JsonArray inventorySlots = new Gson().toJsonTree(PlayerSlots.getUpperInventory().getSlots()).getAsJsonArray();

        if (inventorySlots.contains(slot)) {
            if (lockedSlots.contains(slot)) {
                lockedSlots.remove(slot);
            } else {
                lockedSlots.add(slot);
            }
            FileHandler.write(lockedSlots, LOCKED_SLOTS_PATH, "locked_slots");
        }

    }

    public static void print() {
        System.out.println("MORITZ: " + Arrays.toString(PlayerSlots.getUpperInventoryLockedSlotsExcluded().getSlots()));
    }
}
