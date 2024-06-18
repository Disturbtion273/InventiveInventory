package net.strobel.inventive_inventory;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.command.*;
import net.strobel.inventive_inventory.compat.ModMenuIntegration;
import net.strobel.inventive_inventory.config.ConfigManager;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class InventiveInventory implements ClientModInitializer {
    public static final String MOD_ID = "inventive_inventory";
    public static final String MOD_NAME = "Inventive Inventory";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @Override
    public void onInitializeClient() {
        try {
            GLFW.glfwInit();
            ConfigManager.initialize();
            KeyInputHandler.register();
            KeyInputHandler.registerKeyInputs();
            ClientCommandRegistrationCallback.EVENT.register(LoadProfileCommand::register);
            ClientCommandRegistrationCallback.EVENT.register(SaveProfileCommand::register);
            ClientCommandRegistrationCallback.EVENT.register(DeleteProfileCommand::register);
            ClientCommandRegistrationCallback.EVENT.register(AutomaticRefillingConfigCommand::register);
            ClientCommandRegistrationCallback.EVENT.register(ProfilesConfigCommand::register);
            ClientCommandRegistrationCallback.EVENT.register(SortingConfigCommand::register);
            if (FabricLoader.getInstance().isModLoaded("modmenu")) {
                new ModMenuIntegration().getModConfigScreenFactory();
            }
            LOGGER.info(MOD_NAME + " initialized successfully!");
        } catch (IOException e) {
            LOGGER.error("Couldn't create config files", e);
            LOGGER.error(MOD_NAME + " could not be initialized correctly!");
        }
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
        return MinecraftClient.getInstance().player.currentScreenHandler;
    }

    public static ClientPlayerInteractionManager getInteractionManager() {
        return MinecraftClient.getInstance().interactionManager;
    }
}
