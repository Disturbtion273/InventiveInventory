package net.strobel.inventive_inventory.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.strobel.inventive_inventory.InventiveInventory;

public class ScreenCheck {
    public static boolean isRegularInventory() {
        return InventiveInventory.getScreen() instanceof InventoryScreen;
    }

    public static boolean isCreativeInventory() {
        return InventiveInventory.getScreen() instanceof CreativeInventoryScreen;
    }

    public static boolean isPlayerInventory() {
        Screen screen = InventiveInventory.getScreen();
        return screen instanceof InventoryScreen || screen instanceof CreativeInventoryScreen;
    }

    public static boolean isNull() {
        return InventiveInventory.getScreen() == null;
    }
}
