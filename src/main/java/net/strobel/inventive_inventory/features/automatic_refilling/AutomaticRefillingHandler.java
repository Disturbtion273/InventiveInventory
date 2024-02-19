 package net.strobel.inventive_inventory.features.automatic_refilling;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.handler.InteractionHandler;
import net.strobel.inventive_inventory.slots.InventorySlots;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

 public class AutomaticRefillingHandler {
    private static ItemStack lastItemStack = ItemStack.EMPTY;
    private static int lastItemStackSlot = -1;
    private static final List<Item> ITEMS_TO_CHECK = Arrays.asList(
            Items.BUCKET.asItem(),
            Items.GLASS_BOTTLE.asItem(),
            Items.BOWL.asItem(),
            Items.AIR.asItem());

    public static void run() {
        update();

        ClientPlayerEntity player = InventiveInventory.getPlayer();
        if (player != null && !lastItemStack.isEmpty()) {
            ItemStack currentItemStack = player.getMainHandStack();
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            if (ITEMS_TO_CHECK.contains(currentItemStack.getItem()) && PlayerSlots.getHotbar().stream().noneMatch(slot -> ItemStack.areItemsEqual(lastItemStack, screenHandler.getSlot(slot).getStack()))) {
                Slot sameStack = getFirstOfSameStacks(PlayerSlots.getInventory(), screenHandler);
                if (sameStack != null) InteractionHandler.swapStacks(lastItemStackSlot, sameStack.getIndex());
                reset();
            } else if (ITEMS_TO_CHECK.contains(currentItemStack.getItem()) && PlayerSlots.getHotbar().stream().anyMatch(slot -> ItemStack.areItemsEqual(lastItemStack, screenHandler.getSlot(slot).getStack()))) {
                Slot sameStack = getFirstOfSameStacks(PlayerSlots.getHotbar(), screenHandler);
                if (sameStack != null) player.getInventory().selectedSlot = sameStack.getIndex();
                reset();
            }
        }
    }

    public static void reset() {
        lastItemStack = ItemStack.EMPTY;
        lastItemStackSlot = -1;
    }

    private static void update() {
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        if (player != null) {
            ItemStack currentItemStack = player.getMainHandStack();
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            if (!ITEMS_TO_CHECK.contains(currentItemStack.getItem())) {
                lastItemStack = currentItemStack.copy();
                lastItemStackSlot = PlayerSlots.getHotbar().stream().filter(i -> ItemStack.areItemsEqual(lastItemStack, screenHandler.getSlot(i).getStack())).findFirst().orElse(-1);
            }
        }
    }

    @Nullable
    private static Slot getFirstOfSameStacks(InventorySlots inventorySlots, ScreenHandler screenHandler) {
        return inventorySlots.stream()
                .filter(slot -> ItemStack.areItemsEqual(lastItemStack, screenHandler.getSlot(slot).getStack()))
                .map(screenHandler::getSlot)
                .min(Comparator.comparingInt(slot -> slot.getStack().getCount()))
                .orElse(null);
    }

}
