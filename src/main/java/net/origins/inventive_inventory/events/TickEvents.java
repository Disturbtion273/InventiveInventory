package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;

public class TickEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::checkKeys);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureMainHand);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureOffHand);

        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::automaticRefilling);
    }

    private static void checkKeys(MinecraftClient client) {
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
        if (KeyRegistry.openProfilesScreenKey.isPressed()) {
            InventiveInventory.getClient().setScreen(new ProfilesScreen());
        }
    }

    private static void captureMainHand(MinecraftClient client) {
        if (client.currentScreen == null) {
            AutomaticRefillingHandler.runMainHand();
            if (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed()) {
                AutomaticRefillingHandler.setMainHandStack(InteractionHandler.getMainHandStack());
            } else AutomaticRefillingHandler.reset();
        } else AutomaticRefillingHandler.reset();
    }

    private static void captureOffHand(MinecraftClient client) {
        if (client.currentScreen == null) {
            if (AutomaticRefillingHandler.RUN_OFFHAND) AutomaticRefillingHandler.runOffHand();
            else AutomaticRefillingHandler.RUN_OFFHAND = true;
            if (client.options.useKey.isPressed()) {
                AutomaticRefillingHandler.setOffHandStack(InteractionHandler.getOffHandStack());
            }
        } else AutomaticRefillingHandler.reset();
    }

    private static void automaticRefilling(MinecraftClient client) {
        if (client.currentScreen == null && (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed())) {
            boolean validMode = AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.SEMI_AUTOMATIC
                    || !AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.AUTOMATIC;

            if (validMode) {
                AutomaticRefillingHandler.runMainHand();
                if (AutomaticRefillingHandler.RUN_OFFHAND) {
                    AutomaticRefillingHandler.runOffHand();
                }
            } else AutomaticRefillingHandler.reset();
        }
    }
}
