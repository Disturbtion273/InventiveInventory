package net.strobel.inventive_inventory.handler;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.strobel.inventive_inventory.InventiveInventoryClient;

public class InteractionHandler {
    private static final int LEFT_CLICK = 0;

    public static ItemStack getCursorStack() {
        return InventiveInventoryClient.getScreenHandler().getCursorStack();
    }

    public static boolean hasEmptyCursor() {
        return getCursorStack().isEmpty();
    }

    public static void clickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventoryClient.getInteractionManager();
        ClientPlayerEntity player = InventiveInventoryClient.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
    }

    public static void swapStacks(int slot, int target) {
        ClientPlayerInteractionManager manager = InventiveInventoryClient.getInteractionManager();
        ClientPlayerEntity player = InventiveInventoryClient.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
        manager.clickSlot(getSyncId(), target, LEFT_CLICK, SlotActionType.PICKUP, player);
        if (!InteractionHandler.hasEmptyCursor()) {
            manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
        }
    }

    private static int getSyncId() {
        return InventiveInventoryClient.getScreenHandler().syncId;
    }
}
