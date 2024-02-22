package net.strobel.inventive_inventory.features.automatic_refilling;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.handler.InteractionHandler;
import net.strobel.inventive_inventory.slots.InventorySlots;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.StreamUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public class AutomaticRefillingHandler {
    private static Integer lastSlot;
    private static Item lastItem;
    private static final List<Item> ITEMS_TO_CHECK = Arrays.asList(
            Items.AIR,
            Items.BUCKET,
            Items.GLASS_BOTTLE,
            Items.BOWL
    );

    public static void captureCurrentState(MinecraftClient client) {
        if (client.player != null) {
            ScreenHandler screenHandler = client.player.currentScreenHandler;
            int currentSlot = getCurrentSlot(client.player);
            Item currentItem = screenHandler.getSlot(currentSlot).getStack().getItem();
            if (!ITEMS_TO_CHECK.contains(currentItem)) {
                lastSlot = currentSlot;
                lastItem = screenHandler.getSlot(lastSlot).getStack().getItem();
            }
        }
    }

    public static void run() {
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

        int currentSlot = getCurrentSlot(player);
        Item currentItem = screenHandler.getSlot(currentSlot).getStack().getItem();

        if (ITEMS_TO_CHECK.contains(currentItem) && lastSlot != null) {
            if (currentSlot != lastSlot) return;
            Predicate<Integer> hasSameItem = StreamUtils.sameItem(lastItem, screenHandler);
            if (PlayerSlots.getHotbar().exclude(lastSlot).stream().anyMatch(hasSameItem)) {
                Slot slot = getLowestStack(PlayerSlots.getHotbar(), screenHandler);
                if (slot != null) player.getInventory().selectedSlot = slot.getIndex();
                reset();
            } else if (PlayerSlots.getInventory().stream().anyMatch(hasSameItem)) {
                Slot slot = getLowestStack(PlayerSlots.getInventory(), screenHandler);
                if (slot != null) InteractionHandler.swapStacks(lastSlot, slot.getIndex());
                reset();
            }
        }
    }

    public static void reset() {
        lastSlot = null;
        lastItem = null;
    }

    private static int getCurrentSlot(ClientPlayerEntity player) {
        return player.currentScreenHandler.getSlotIndex(player.getInventory(), player.getInventory().selectedSlot).orElse(-1);
    }

    @Nullable
    private static Slot getLowestStack(InventorySlots inventorySlots, ScreenHandler screenHandler) {
        return inventorySlots.stream()
                .filter(StreamUtils.sameItem(lastItem, screenHandler))
                .map(screenHandler::getSlot)
                .min(Comparator.comparingInt(slot -> slot.getStack().getCount()))
                .orElse(null);
    }

}
