package net.strobel.inventive_inventory.slots;

import com.google.gson.JsonElement;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.Arrays;
import java.util.List;

public class PlayerSlots {
    private static final PlayerInventory inventory = InventiveInventoryClient.getPlayer().getInventory();
    private static final int INVENTORY = inventory.main.size();
    private static final int HOTBAR = 9;
    private static final int OFFHAND = inventory.offHand.size();
    private static final int CREATIVE_DELETE_SLOT = 1;
    public static InventorySlots getFullInventory() {
        ScreenHandler screenHandler = InventiveInventoryClient.getScreenHandler();
        int from;
        int to;
        if (screenHandler instanceof PlayerScreenHandler) {
            from = screenHandler.slots.size() - (INVENTORY + OFFHAND);
            to = screenHandler.slots.size();
        } else if (screenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            from = screenHandler.slots.size() - (INVENTORY + OFFHAND + CREATIVE_DELETE_SLOT);
            to = screenHandler.slots.size() - CREATIVE_DELETE_SLOT;
        } else {
            from = screenHandler.slots.size() - INVENTORY;
            to = screenHandler.slots.size();
        }
        return new InventorySlots(from, to);
    }

    public static InventorySlots getUpperInventory() {
        ScreenHandler screenHandler = InventiveInventoryClient.getScreenHandler();
        int from;
        int to;
        if (screenHandler instanceof PlayerScreenHandler) {
            from = screenHandler.slots.size() - (INVENTORY + OFFHAND);
            to = screenHandler.slots.size() - (HOTBAR + OFFHAND);
            return new InventorySlots(from, to, 45);
        } else if (screenHandler instanceof CreativeInventoryScreen.CreativeScreenHandler) {
            from = screenHandler.slots.size() - (INVENTORY + OFFHAND + CREATIVE_DELETE_SLOT);
            to = screenHandler.slots.size() - (HOTBAR + OFFHAND + CREATIVE_DELETE_SLOT);
            return new InventorySlots(from, to, 45);
        } else {
            from = screenHandler.slots.size() - INVENTORY;
            to = screenHandler.slots.size() - HOTBAR;
            return new InventorySlots(from, to);
        }
    }

    public static InventorySlots getUpperInventoryLockedSlotsExcluded() {
        List<JsonElement> lockedSlots = FileHandler.get(InventiveInventoryClient.CONFIG_PATH + "locked_slots.json").asList();
        return new InventorySlots(Arrays.stream(getUpperInventory().getSlots())
                .filter(slot -> lockedSlots.stream().noneMatch(element -> slot == element.getAsInt() && slot != 45))
                .toArray());
    }
}
