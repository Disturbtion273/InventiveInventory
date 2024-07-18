package net.origins.inventive_inventory.util;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.ProfilesScreen;

public class ScreenCheck {
    public static boolean isSurvivalInventory() {
        return InventiveInventory.getScreen() instanceof InventoryScreen;
    }

    public static boolean isCreativeInventory() {
        return InventiveInventory.getScreen() instanceof CreativeInventoryScreen;
    }

    public static boolean isPlayerInventory() {
        return isSurvivalInventory() || isCreativeInventory();
    }

    public static boolean isPlayerHandler() {
        return InventiveInventory.getScreenHandler() instanceof PlayerScreenHandler;
    }

    public static boolean isProfileScreen() {
        return InventiveInventory.getScreen() instanceof ProfilesScreen;
    }
}
