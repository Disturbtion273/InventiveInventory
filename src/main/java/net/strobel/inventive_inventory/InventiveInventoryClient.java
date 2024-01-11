package net.strobel.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.strobel.inventive_inventory.event.KeyInputHandler;


public class InventiveInventoryClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }
}
