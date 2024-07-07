package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.origins.inventive_inventory.features.profiles.ProfilesScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;

public class TickEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::checkKeys);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureMainHandItem);

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

    private static void captureMainHandItem(MinecraftClient client) {
        if (client.currentScreen == null) {
            if (AutomaticRefillingHandler.ATTACK_KEY_PRESSED) {
                AutomaticRefillingHandler.run();
                AutomaticRefillingHandler.ATTACK_KEY_PRESSED = false;
            }
            AutomaticRefillingHandler.setSelectedItem(InventiveInventory.getPlayer().getMainHandStack());
        }
    }

    private static void automaticRefilling(MinecraftClient client) {
        if (client.currentScreen == null && (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed())) {
            if (ConfigManager.AR_MODE == AutomaticRefillingModes.AUTOMATIC) {
                if (!AdvancedOperationHandler.isPressed()) {
                    AutomaticRefillingHandler.run();
                }
            } else if (ConfigManager.AR_MODE == AutomaticRefillingModes.SEMI_AUTOMATIC) {
                if (AdvancedOperationHandler.isPressed()) {
                    AutomaticRefillingHandler.run();
                }
            }
            if (client.options.attackKey.isPressed()) AutomaticRefillingHandler.ATTACK_KEY_PRESSED = true;
        }
    }

}
