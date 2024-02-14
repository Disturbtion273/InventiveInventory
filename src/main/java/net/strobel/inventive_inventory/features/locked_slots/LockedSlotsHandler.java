package net.strobel.inventive_inventory.features.locked_slots;

import net.strobel.inventive_inventory.config.ConfigManager;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;
import net.strobel.inventive_inventory.util.MousePosition;

import java.nio.file.Path;

public class LockedSlotsHandler {

    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = ConfigManager.PATH.resolve(LOCKED_SLOTS_FILE);

    public static void toggle() {
        int slot = MousePosition.getHoveredSlot();
        LockedSlots lockedSlots = getLockedSlots();

        if (PlayerSlots.get().excludeOffhand().contains(slot)) {
            lockedSlots.toggle(slot);
            FileHandler.write(LOCKED_SLOTS_PATH, "locked_slots", lockedSlots);
        }
    }

    public static LockedSlots getLockedSlots() {
        return new LockedSlots(FileHandler.get(LOCKED_SLOTS_PATH, "locked_slots"));
    }
}
