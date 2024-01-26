package net.strobel.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.events.KeyInputHandler;


public class InventiveInventoryClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity getPlayer() {return MinecraftClient.getInstance().player;}

    public static ScreenHandler getScreenHandler() {return MinecraftClient.getInstance().player.currentScreenHandler;}

    public static ClientPlayerInteractionManager getInteractionManager() {return MinecraftClient.getInstance().interactionManager;}
}
