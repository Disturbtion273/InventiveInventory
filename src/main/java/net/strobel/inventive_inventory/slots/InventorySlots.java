package net.strobel.inventive_inventory.slots;

import java.util.stream.IntStream;

public class InventorySlots {
    private final int[] slots;

    public InventorySlots(int from, int to) {
        this.slots = IntStream.range(from, to).toArray();
    }

    public InventorySlots(int from, int to, int offhand) {
        this.slots = IntStream.concat(IntStream.range(from, to), IntStream.of(offhand)).toArray();
    }

    public int[] getSlots() {
        return this.slots;
    }

    public int getFirstSlot() {
        return this.slots[0];
    }

    public int getLastSlot() {
        return this.slots[slots.length - 1];
    }
}
