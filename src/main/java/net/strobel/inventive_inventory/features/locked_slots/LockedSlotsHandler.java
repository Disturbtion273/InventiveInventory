package net.strobel.inventive_inventory.features.locked_slots;

import net.minecraft.screen.PlayerScreenHandler;
import net.strobel.inventive_inventory.config.ConfigManager;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;
import net.strobel.inventive_inventory.util.MousePosition;

import java.nio.file.Path;
import java.util.List;

public class LockedSlotsHandler {

    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = ConfigManager.PATH.resolve(LOCKED_SLOTS_FILE);

    public static void toggle() {
        int slot = MousePosition.getHoveredSlot();
        LockedSlots lockedSlots = LockedSlotsHandler.get();
        List<Integer> inventorySlots = PlayerSlots.get(false);
        inventorySlots.remove(Integer.valueOf(PlayerScreenHandler.OFFHAND_ID));

        if (inventorySlots.contains(slot)) {
            lockedSlots.toggle(slot);
            FileHandler.write(LOCKED_SLOTS_PATH, "locked_slots", lockedSlots);
        }
    }

    public static LockedSlots get() {
        return new LockedSlots(FileHandler.get(LOCKED_SLOTS_PATH, "locked_slots"));
    }
}
