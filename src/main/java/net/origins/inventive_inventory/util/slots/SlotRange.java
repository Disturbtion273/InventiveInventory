package net.origins.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.origins.inventive_inventory.util.ScreenCheck;

import java.util.ArrayList;
import java.util.stream.IntStream;

public class SlotRange extends ArrayList<Integer> {

    public SlotRange(int start, int stop) {
        super(IntStream.range(start, stop).boxed().toList());
    }

    public SlotRange append(SlotTypes type) {
        switch (type) {
            case SlotTypes.HOTBAR:
                int lastSlot = (this.getLast() == PlayerScreenHandler.OFFHAND_ID ? this.get(this.size() - 2) : this.getLast()) + PlayerInventory.getHotbarSize();
                IntStream.rangeClosed(this.getLast() + 1, lastSlot).forEach(this::add);
                break;
            case SlotTypes.OFFHAND:
                if (ScreenCheck.isPlayerInventory()) this.add(PlayerScreenHandler.OFFHAND_ID);
                break;
        }
        return this;
    }

    public SlotRange exclude(SlotTypes type) {
        return null;
    }

    public SlotRange exclude(Integer slot) {
        this.remove(slot);
        return this;
    }
    public SlotRange copy() {
        return (SlotRange) this.clone();
    }
}
