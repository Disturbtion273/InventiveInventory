package net.strobel.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.handler.KeyInputHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


public class InventiveInventoryClient implements ClientModInitializer {
    public static final String CONFIG_PATH = "config/inventive_inventory/";

    @Override
    public void onInitializeClient() {
        createConfigs();
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

    public static void createConfigs() {
        try {
            Files.createDirectories(Path.of(CONFIG_PATH));
            Files.createFile(Path.of(LockedSlotsHandler.LOCKED_SLOTS_PATH));
        } catch (IOException ignored) {}
    }
}
