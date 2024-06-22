package net.origins.inventive_inventory.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.origins.inventive_inventory.InventiveInventory;

public class InteractionHandler {
    private static final int LEFT_CLICK = 0;
    private static final int RIGHT_CLICK = 1;

    public static ItemStack getCursorStack() {
        return InventiveInventory.getScreenHandler().getCursorStack();
    }

    public static boolean isCursorEmpty() {
        return getCursorStack().isEmpty();
    }

    public static void leftClickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
    }

    public static void rightClickStack(int slot) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, RIGHT_CLICK, SlotActionType.PICKUP, player);
    }

    public static void swapStacks(int slot, int target) {
        ClientPlayerInteractionManager manager = InventiveInventory.getInteractionManager();
        ClientPlayerEntity player = InventiveInventory.getPlayer();
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
        manager.clickSlot(getSyncId(), target, LEFT_CLICK, SlotActionType.PICKUP, player);
        manager.clickSlot(getSyncId(), slot, LEFT_CLICK, SlotActionType.PICKUP, player);
    }

    private static int getSyncId() {
        return InventiveInventory.getScreenHandler().syncId;
    }
}
