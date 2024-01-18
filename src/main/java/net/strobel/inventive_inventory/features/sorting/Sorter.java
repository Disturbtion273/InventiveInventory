package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;
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
        //TO BE DONE!!!
    }

    public static Inventory getPlayerInventory() {
        return player.getInventory();
    }

    public static Inventory getContainerInventory() {
        GenericContainerScreenHandler handler = (GenericContainerScreenHandler) player.currentScreenHandler;
        return handler.getInventory();
    }

    public static void updateInstances() {
        client = InventiveInventoryClient.getClient();
        player = client.player;
        interactionManager = client.interactionManager;
    }
}
