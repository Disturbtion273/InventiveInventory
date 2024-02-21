package net.strobel.inventive_inventory.slots;

import net.minecraft.screen.PlayerScreenHandler;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.util.ScreenCheck;

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

    public InventorySlots(int from, int to, int from2, int to2) {
        super(IntStream.concat(IntStream.range(from, to), IntStream.rangeClosed(from2, to2)).boxed().toList());
    }

    public InventorySlots(List<Integer> slots) {
        super(slots);
    }

    public int getLastSlot() {
        return this.get(this.size() - 1);
    }

    public InventorySlots excludeLockedSlots() {
        List<Integer> lockedSlots = LockedSlotsHandler.getLockedSlots().adjust();
        return new InventorySlots(this.stream().filter(slot -> !lockedSlots.contains(slot)).toList());
    }

    public InventorySlots excludeOffhand() {
        if (ScreenCheck.isPlayerInventory()) {
            this.remove(Integer.valueOf(PlayerScreenHandler.OFFHAND_ID));
            return new InventorySlots(this);
        }
        return this;
    }

    public InventorySlots exclude(Integer slot) {
        this.remove(slot);
        return new InventorySlots(this);
    }
}
