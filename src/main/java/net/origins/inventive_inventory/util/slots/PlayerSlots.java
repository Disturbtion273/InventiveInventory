package net.origins.inventive_inventory.util.slots;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;

import java.util.ArrayList;
import java.util.List;

public class PlayerSlots {
    public static final int HOTBAR_SIZE = 9;
    public static final int OFFHAND_SIZE = 1;

    public static SlotRange get() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        int start = screenHandler.slots.size() - PlayerInventory.MAIN_SIZE - (screenHandler instanceof PlayerScreenHandler ? OFFHAND_SIZE : 0);
        int stop = screenHandler.slots.size() - HOTBAR_SIZE - (screenHandler instanceof PlayerScreenHandler ? OFFHAND_SIZE : 0);
        return new SlotRange(start, stop);
    }

    public static SlotRange get(SlotTypes... types) {
        SlotRange slotRange = SlotRange.of(new ArrayList<>());
        for (SlotTypes type : types) {
            if (List.of(SlotTypes.HOTBAR, SlotTypes.INVENTORY, SlotTypes.OFFHAND).contains(type)) slotRange.append(type);
            else throw new IllegalArgumentException("This SlotType is not valid in this function");
        }
        return slotRange;
    }
}