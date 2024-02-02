package net.strobel.inventive_inventory.slots;

import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlots;
import net.strobel.inventive_inventory.util.Utilities;

import java.util.List;
import java.util.stream.IntStream;

public class InventorySlots {
    private final List<Integer> slots;

    public InventorySlots(int from, int to) {
        this.slots = IntStream.range(from, to).boxed().toList();
    }

    public InventorySlots(int from, int to, int offhand) {
        this.slots = IntStream.concat(IntStream.range(from, to), IntStream.of(offhand)).boxed().toList();
    }

    public InventorySlots(List<Integer> slots) {
        this.slots = slots;
    }

    public List<Integer> getSlots() {
        return this.slots;
    }

    public int getFirstSlot() {
        return this.slots.get(0);
    }

    public int getLastSlot() {
        return this.slots.get(this.slots.size() - 1);
    }

    public InventorySlots excludeLockedSlots() {
        List<Integer> lockedSlots = Utilities.adjustLockedSlots(LockedSlots.get(), InventiveInventoryClient.getScreenHandler());
        return new InventorySlots(this.slots.stream().filter(slot -> !lockedSlots.contains(slot) && slot != 45).toList());

    }
}
