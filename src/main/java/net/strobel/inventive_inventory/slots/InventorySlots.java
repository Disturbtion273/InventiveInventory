package net.strobel.inventive_inventory.slots;

import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class InventorySlots extends ArrayList<Integer> {

    public InventorySlots(int from, int to) {
        super(IntStream.range(from, to).boxed().toList());
    }

    public InventorySlots(int from, int to, int offhand) {
        super(IntStream.concat(IntStream.range(from, to), IntStream.of(offhand)).boxed().toList());
    }

    public InventorySlots(List<Integer> slots) {
        super(slots);
    }

    public int getLastSlot() {
        return this.get(this.size() - 1);
    }

    public InventorySlots excludeLockedSlots() {
        List<Integer> lockedSlots = LockedSlotsHandler.get().adjust();
        return new InventorySlots(this.stream().filter(slot -> !lockedSlots.contains(slot)).toList());

    }
}
