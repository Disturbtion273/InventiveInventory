package net.origins.inventive_inventory.features.locked_slots;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.util.FileHandler;
import net.origins.inventive_inventory.util.mouse.MouseLocation;
import net.origins.inventive_inventory.util.slots.PlayerSlots;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class LockedSlotsHandler {
    private static final String LOCKED_SLOTS_FILE = "locked_slots.json";
    public static final Path LOCKED_SLOTS_PATH = ConfigManager.CONFIG_PATH.resolve(LOCKED_SLOTS_FILE);

    public static void toggle() {
        Slot slot = MouseLocation.getHoveredSlot();
        if (slot == null) return;
        LockedSlots lockedSlots = getLockedSlots();

        if (PlayerSlots.get().contains(slot.getIndex())) {
            if (lockedSlots.contains(slot.getIndex())) {
                lockedSlots.remove(Integer.valueOf(slot.getIndex()));
            } else lockedSlots.add(slot.getIndex());
            JsonArray lockedSlotsJson = new JsonArray();
            for (int lockedSlot : lockedSlots) {
                lockedSlotsJson.add(lockedSlot);
            }
            FileHandler.write(LOCKED_SLOTS_PATH, lockedSlotsJson);
        }
    }

    public static LockedSlots getLockedSlots() {
        List<Integer> lockedSlots = new ArrayList<>();
        JsonArray lockedSlotsJson = FileHandler.get(LOCKED_SLOTS_PATH).isJsonArray() ? FileHandler.get(LOCKED_SLOTS_PATH).getAsJsonArray() : new JsonArray();
        for (JsonElement slot : lockedSlotsJson) {
            lockedSlots.add(slot.getAsInt());
        }
        return new LockedSlots(lockedSlots);
    }
}
