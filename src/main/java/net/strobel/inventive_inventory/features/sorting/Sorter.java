package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.InventiveInventoryClient;

public class Sorter {

    public static MinecraftClient client;
    public static ClientPlayerEntity player;
    public static ClientPlayerInteractionManager interactionManager;

    public static void sortPlayerInventory() {
        updateInstances();
        int syncId = player.currentScreenHandler.syncId;
        PlayerSorter.sortInventory(syncId);
    }

    public static void sortContainerInventory() {
        updateInstances();
        int syncId = player.currentScreenHandler.syncId;
        ContainerSorter.sortContainer(syncId);
    }

    public static Inventory getPlayerInventory() {
        return player.getInventory();
    }

    public static Inventory getContainerInventory() {
        GenericContainerScreenHandler handler = (GenericContainerScreenHandler) player.currentScreenHandler;
        return handler.getInventory();
    }

    public static void swapItems(int slot, int swapWith, int syncId) {
        Sorter.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, Sorter.player);
        Sorter.interactionManager.clickSlot(syncId, swapWith, 0, SlotActionType.PICKUP, Sorter.player);
        Sorter.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, Sorter.player);
    }

    public static void combineStack(int slot, int combineWith, boolean isTooMuch, int syncId) {
        Sorter.interactionManager.clickSlot(syncId, slot, 0, SlotActionType.PICKUP, Sorter.player);
        Sorter.interactionManager.clickSlot(syncId, combineWith, 0, SlotActionType.PICKUP, Sorter.player);
        if (isTooMuch) {
            Sorter.player.sendMessage(Text.of("Is Too Much"));
            Sorter.interactionManager.clickSlot(syncId, combineWith, 0, SlotActionType.PICKUP, Sorter.player);
        }
    }

    public static void updateInstances() {
        client = InventiveInventoryClient.getClient();
        player = client.player;
        interactionManager = client.interactionManager;
    }
}
