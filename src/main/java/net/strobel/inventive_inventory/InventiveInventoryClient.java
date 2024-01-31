package net.strobel.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.util.FileHandler;


public class InventiveInventoryClient implements ClientModInitializer {
    public static final String CONFIG_PATH = "config/inventive_inventory/";
    @Override
    public void onInitializeClient() {
        KeyInputHandler.register();
        KeyInputHandler.registerKeyInputs();
        FileHandler.createConfigs();
    }

    public static MinecraftClient getClient() {
        return MinecraftClient.getInstance();
    }

    public static ClientPlayerEntity getPlayer() {return MinecraftClient.getInstance().player;}

    public static Screen getScreen() {return MinecraftClient.getInstance().currentScreen;}

    public static ScreenHandler getScreenHandler() {return MinecraftClient.getInstance().player.currentScreenHandler;}

    public static ClientPlayerInteractionManager getInteractionManager() {return MinecraftClient.getInstance().interactionManager;}
}
