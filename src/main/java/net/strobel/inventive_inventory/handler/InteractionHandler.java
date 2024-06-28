package net.strobel.inventive_inventory.handler;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.strobel.inventive_inventory.InventiveInventory;

public class InteractionHandler {
    private static final int LEFT_CLICK = 0;

    public static ItemStack getCursorStack() {
        return InventiveInventory.getScreenHandler().getCursorStack();
    }

    public static boolean hasEmptyCursor() {
        return getCursorStack().isEmpty();
    }

    public static ItemStack getStackFromSlot(int slot) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        return screenHandler.getSlot(slot).getStack();
    }

    public static int getSelectedSlot() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        return screenHandler.getSlotIndex(player.getInventory(), player.getInventory().selectedSlot).orElse(-1);
    }

    public static void clickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
    }

    public static void swapStacks(int slot, int target) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
        manager.clickSlot(getSyncId(), target, LEFT_CLICK, SlotActionType.PICKUP, player);
        if (!InteractionHandler.hasEmptyCursor()) {
            manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
        }
    }

    private static int getSyncId() {
        return InventiveInventory.getScreenHandler().syncId;
    }
}
