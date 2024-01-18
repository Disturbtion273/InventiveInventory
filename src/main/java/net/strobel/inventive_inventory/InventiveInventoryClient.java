package net.strobel.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.strobel.inventive_inventory.event.DisconnectHandler;
import net.strobel.inventive_inventory.event.KeyInputHandler;
import net.strobel.inventive_inventory.features.sorting.Sorter;


public class InventiveInventoryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        Sorter.updateInstances();
        DisconnectHandler.register();
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }
}
