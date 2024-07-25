package net.origins.inventive_inventory.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingModes;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingToolBreakingBehaviour;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesLoadMode;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesStatus;
import net.origins.inventive_inventory.features.automatic_refilling.AutomaticRefillingHandler;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesConfigScreen;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesScreen;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;

import java.util.List;

public class TickEvents {

    public static void register() {
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::checkKeys);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureMainHand);
        ClientTickEvents.START_CLIENT_TICK.register(TickEvents::captureOffHand);

        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::automaticRefilling);
        ClientTickEvents.END_CLIENT_TICK.register(TickEvents::loadProfile);
    }

    private static void checkKeys(MinecraftClient client) {
        if (InventiveInventory.getPlayer() != null && InventiveInventory.getPlayer().isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AdvancedOperationHandler.setPressed(KeyRegistry.advancedOperationKey.isPressed());
        }
        if (KeyRegistry.openProfilesScreenKey.isPressed() && ConfigManager.PROFILES == ProfilesStatus.ENABLED) {
            InventiveInventory.getClient().setScreen(new ProfilesScreen());
        }
        if (ProfilesConfigScreen.SHOULD_BE_SET) InventiveInventory.getClient().setScreen(new ProfilesConfigScreen(client.currentScreen));
    }

    private static void captureMainHand(MinecraftClient client) {
        if (InventiveInventory.getPlayer() != null && InventiveInventory.getPlayer().isInCreativeMode()) return;
        if (client.currentScreen == null) {
            AutomaticRefillingHandler.runMainHand();
            if (client.options.useKey.isPressed() || client.options.dropKey.isPressed() || client.options.attackKey.isPressed()) {
                if (!(ConfigManager.AR_TOOL_BREAKING_BEHAVIOUR == AutomaticRefillingToolBreakingBehaviour.KEEP_TOOL && AutomaticRefillingHandler.TOOL_CLASSES.contains(InteractionHandler.getMainHandStack().getItem().getClass()) && InteractionHandler.getMainHandStack().getMaxDamage() - InteractionHandler.getMainHandStack().getDamage() == 1)) {
                    AutomaticRefillingHandler.setMainHandStack(InteractionHandler.getMainHandStack());
                }
            } else AutomaticRefillingHandler.reset();
        } else AutomaticRefillingHandler.reset();
    }

    private static void captureOffHand(MinecraftClient client) {
        if (InventiveInventory.getPlayer() != null && InventiveInventory.getPlayer().isInCreativeMode()) return;
        if (client.currentScreen == null) {
            if (AutomaticRefillingHandler.RUN_OFFHAND) AutomaticRefillingHandler.runOffHand();
            else AutomaticRefillingHandler.RUN_OFFHAND = true;
            if (client.options.useKey.isPressed()) {
                if (!(ConfigManager.AR_TOOL_BREAKING_BEHAVIOUR == AutomaticRefillingToolBreakingBehaviour.KEEP_TOOL && AutomaticRefillingHandler.TOOL_CLASSES.contains(InteractionHandler.getOffHandStack().getItem().getClass()) && InteractionHandler.getOffHandStack().getMaxDamage() - InteractionHandler.getOffHandStack().getDamage() == 1)) {
                    AutomaticRefillingHandler.setOffHandStack(InteractionHandler.getOffHandStack());
                }
            }
        } else AutomaticRefillingHandler.reset();
    }

    private static void automaticRefilling(MinecraftClient client) {
        if (InventiveInventory.getPlayer() != null && InventiveInventory.getPlayer().isInCreativeMode()) return;
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

    private static void loadProfile(MinecraftClient ignored) {
        if (InventiveInventory.getPlayer() != null && InventiveInventory.getPlayer().isInCreativeMode()) return;
        for (KeyBinding profileKey : KeyRegistry.profileKeys) {
            if (profileKey.isPressed()) {
                boolean validMode = ConfigManager.P_LOAD_MODE == ProfilesLoadMode.FAST_LOAD || (ConfigManager.P_LOAD_MODE == ProfilesLoadMode.STANDARD_LOAD && KeyRegistry.loadProfileKey.isPressed());
                if (validMode) {
                    List<Profile> profiles = ProfileHandler.getProfiles();
                    profiles.forEach(profile -> {
                        if (profileKey.getTranslationKey().equals(profile.getKey())) ProfileHandler.load(profile);
                    });
                }
            }
        }
    }
}
