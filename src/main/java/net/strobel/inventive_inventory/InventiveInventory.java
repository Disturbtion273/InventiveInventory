package net.strobel.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.config.ConfigManager;
import net.strobel.inventive_inventory.handler.KeyInputHandler;

public class InventiveInventory implements ClientModInitializer {
    public static final String MOD_ID = "inventive_inventory";

    @Override
    public void onInitializeClient() {
        ConfigManager.initialize();
        KeyInputHandler.register();
        KeyInputHandler.registerKeyInputs();
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity getPlayer() {
        return MinecraftClient.getInstance().player;
    }

    public static Screen getScreen() {
        return MinecraftClient.getInstance().currentScreen;
    }

    public static ScreenHandler getScreenHandler() {
        if (MinecraftClient.getInstance().player != null) {
            return MinecraftClient.getInstance().player.currentScreenHandler;
        }
        return null;
    }

    public static ClientPlayerInteractionManager getInteractionManager() {
        return MinecraftClient.getInstance().interactionManager;
    }
}
