package net.strobel.inventive_inventory.features.locked_slots;

import com.google.gson.Gson;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;
import net.strobel.inventive_inventory.util.JsonHandler;
import net.strobel.inventive_inventory.util.MousePosition;

import java.util.List;

public class LockedSlots {

    public static final String LOCKED_SLOTS_PATH = InventiveInventoryClient.CONFIG_PATH + "locked_slots.json";

    public static void set() {
        Integer slot = MousePosition.getHoveredSlot();
        List<Integer> lockedSlots = JsonHandler.jsonArrayToIntegerList(FileHandler.get(LOCKED_SLOTS_PATH));
        List<Integer> inventorySlots = PlayerSlots.get(false).getSlots();

        if (inventorySlots.contains(slot) && slot != 45) {
            if (lockedSlots.contains(slot)) {
                lockedSlots.remove(slot);
                System.out.println("Unlocked Slot!");
            } else {
                lockedSlots.add(slot);
                System.out.println("Locked Slot!");
            }
            FileHandler.write(new Gson().toJsonTree(lockedSlots).getAsJsonArray(), LOCKED_SLOTS_PATH, "locked_slots");
        }
    }

    public static List<Integer> get() {
        return JsonHandler.jsonArrayToIntegerList(FileHandler.get(LOCKED_SLOTS_PATH));
    }
}
