package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.origins.inventive_inventory.features.profiles.ProfilesScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;

public class TickEvents {
    // TODO: Capture Items only when clicked!

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::checkKeys);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureMainHandItem);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureOffHand);

        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::automaticRefilling);
        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::runOffHand);
    }

    private static void checkKeys(MinecraftClient client) {
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }

        if (KeyRegistry.openProfilesScreenKey.isPressed()) {
            InventiveInventory.getClient().setScreen(new ProfilesScreen());
        }
    }

    private static void captureMainHandItem(MinecraftClient client) {
        if (client.currentScreen == null) {
            boolean validMode = AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.SEMI_AUTOMATIC
                    || !AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.AUTOMATIC;
            if ( AutomaticRefillingHandler.USE_KEY_PRESSED && validMode) {
                AutomaticRefillingHandler.run();
                AutomaticRefillingHandler.USE_KEY_PRESSED = false;
            } else if (AutomaticRefillingHandler.ATTACK_KEY_PRESSED && validMode) {
                AutomaticRefillingHandler.run();
                AutomaticRefillingHandler.ATTACK_KEY_PRESSED = false;
            } else if (AutomaticRefillingHandler.IS_USING_ITEM && validMode) {
                AutomaticRefillingHandler.run();
                AutomaticRefillingHandler.IS_USING_ITEM = false;
            }
            AutomaticRefillingHandler.setSelectedItem(InventiveInventory.getPlayer().getMainHandStack());
        }
    }

    private static void automaticRefilling(MinecraftClient client) {
        if (client.currentScreen == null && (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed())) {
            boolean validMode = AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.SEMI_AUTOMATIC
                    || !AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.AUTOMATIC;

            if (validMode) AutomaticRefillingHandler.run();

            if (client.options.useKey.isPressed()) AutomaticRefillingHandler.USE_KEY_PRESSED = true;
            if (client.options.attackKey.isPressed()) AutomaticRefillingHandler.ATTACK_KEY_PRESSED = true;
            if (InventiveInventory.getPlayer().isUsingItem()) AutomaticRefillingHandler.IS_USING_ITEM = true;
        }
    }

    private static void captureOffHand(MinecraftClient client) {
        if (client.currentScreen == null) {
            boolean validMode = AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.SEMI_AUTOMATIC
                    || !AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.AUTOMATIC;
            if ( AutomaticRefillingHandler.USE_KEY_PRESSED && validMode) {
                AutomaticRefillingHandler.runOffHand();
                AutomaticRefillingHandler.USE_KEY_PRESSED = false;
            } else if (AutomaticRefillingHandler.IS_USING_ITEM && validMode) {
                AutomaticRefillingHandler.runOffHand();
                AutomaticRefillingHandler.IS_USING_ITEM = false;
            }
            AutomaticRefillingHandler.setOffhandItem(InteractionHandler.getOffHandStack());
        }
    }

    private static void runOffHand(MinecraftClient client) {
        if (client.currentScreen == null && client.options.useKey.isPressed()) {
            if (AutomaticRefillingHandler.RUN_OFFHAND) {
                boolean validMode = AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.SEMI_AUTOMATIC
                        || !AdvancedOperationHandler.isPressed() && ConfigManager.AR_MODE == AutomaticRefillingModes.AUTOMATIC;
                if (validMode) AutomaticRefillingHandler.runOffHand();
            } else {
                AutomaticRefillingHandler.RUN_OFFHAND = true;
                AutomaticRefillingHandler.setOffhandItem(Items.AIR.getDefaultStack());
            }
        }
    }
}
