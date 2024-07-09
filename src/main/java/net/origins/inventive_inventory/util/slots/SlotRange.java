package net.origins.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.util.ScreenCheck;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class SlotRange extends ArrayList<Integer> {

    public SlotRange(int start, int stop) {
        super(IntStream.range(start, stop).boxed().toList());
    }

    private SlotRange(List<Integer> list) {
        super(list);
    }

    public static SlotRange of(List<Integer> list) {
        return new SlotRange(list);
    }

    public SlotRange append(SlotTypes type) {
        if (type == SlotTypes.HOTBAR) {
            int lastSlot = (this.getLast() == PlayerScreenHandler.OFFHAND_ID ? this.get(this.size() - 2) : this.getLast()) + PlayerInventory.getHotbarSize();
            IntStream.rangeClosed(this.getLast() + 1, lastSlot).forEach(this::add);
        } else if (type == SlotTypes.OFFHAND) {
            if (ScreenCheck.isPlayerHandler()) this.add(PlayerScreenHandler.OFFHAND_ID);
        } return this;
    }

    public SlotRange exclude(SlotTypes type) {
        if (type == SlotTypes.LOCKED_SLOT) LockedSlotsHandler.getLockedSlots().adjust().forEach(this::remove);
        else if (type == SlotTypes.INVENTORY) PlayerSlots.get().forEach(this::remove);
        else if (type == SlotTypes.HOTBAR) PlayerSlots.get(SlotTypes.HOTBAR).forEach(this::remove);
        return this;
    }

    public SlotRange exclude(Integer slot) {
        this.remove(slot);
        return this;
    }

    public SlotRange copy() {
        return (SlotRange) this.clone();
    }
}
