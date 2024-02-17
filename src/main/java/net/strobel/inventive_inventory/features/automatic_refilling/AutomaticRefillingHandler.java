package net.strobel.inventive_inventory.features.automatic_refilling;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.handler.InteractionHandler;
import net.strobel.inventive_inventory.slots.PlayerSlots;

public class AutomaticRefillingHandler {
    public static ItemStack lastItemStack = ItemStack.EMPTY;
    public static int lastItemStackSlot = -1;

    private static void update() {
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        if (player != null) {
            ItemStack currentItemStack = player.getMainHandStack();
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            if (!currentItemStack.isEmpty()) {
                lastItemStack = currentItemStack.copy();
                lastItemStackSlot = PlayerSlots.getHotbar().stream().filter(i -> ItemStack.areItemsEqual(lastItemStack, screenHandler.getSlot(i).getStack())).findFirst().orElse(-1);
            }
        }
    }

    public static void run() {
        update();

        ClientPlayerEntity player = InventiveInventory.getPlayer();
        if (player != null && !lastItemStack.isEmpty()) {
            ItemStack currentItemStack = player.getMainHandStack();
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
            if (currentItemStack.isEmpty() && PlayerSlots.getHotbar().stream().noneMatch(slot -> ItemStack.areItemsEqual(lastItemStack, screenHandler.getSlot(slot).getStack()))) {
                for (int slot: PlayerSlots.getInventory()) {
                    if (ItemStack.areItemsEqual(lastItemStack, player.getInventory().getStack(slot))) {
                        InteractionHandler.swapStacks(lastItemStackSlot, slot);
                        break;
                    }
                }
                lastItemStack = ItemStack.EMPTY;
                lastItemStackSlot = -1;
            }
        }
    }
}
