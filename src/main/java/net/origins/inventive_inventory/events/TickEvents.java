package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;

public class TickEvents {
    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureSelectedItem);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::checkKeys);

        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::automaticRefilling);
    }

    private static void captureSelectedItem(MinecraftClient client) {
        if (client.currentScreen == null) {
            if ((ConfigManager.AUTOMATIC_REFILLING_MODE == AutomaticRefillingModes.STANDARD && AdvancedOperationHandler.isPressed())
                    || ConfigManager.AUTOMATIC_REFILLING_MODE == AutomaticRefillingModes.INVERTED) {
                AutomaticRefillingHandler.setSelectedItem(InventiveInventory.getPlayer().getMainHandStack());
            }
        }

    }

    private static void checkKeys(MinecraftClient client) {
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
    }

    private static void automaticRefilling(MinecraftClient client) {
        if (client.currentScreen == null) {
            if (ConfigManager.AUTOMATIC_REFILLING_MODE == AutomaticRefillingModes.STANDARD) {
                if (AdvancedOperationHandler.isPressed()) {
                    AutomaticRefillingHandler.run();
                }
            }
        }
    }

}
