package net.strobel.inventive_inventory;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InventiveInventory implements ModInitializer {
    public static final String MOD_ID = "inventive_inventory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Welcome to Inventive Inventory!");
    }
}