package net.strobel.inventive_inventory.handler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.strobel.inventive_inventory.util.ScreenCheck;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    private static final String INVENTIVE_INVENTORY_CATEGORY = "key.inventive_inventory.category.inventive_inventory";
    private static final String KEY_ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";
    private static final String KEY_SORT_INVENTORY = "key.inventive_inventory.sort_inventory";
    public static KeyBinding advancedOperationKey;
    public static KeyBinding sortInventoryKey;


    public static void register() {
        advancedOperationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ADVANCED_OPERATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                INVENTIVE_INVENTORY_CATEGORY
        ));
        sortInventoryKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SORT_INVENTORY,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                INVENTIVE_INVENTORY_CATEGORY
        ));
    }

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (ScreenCheck.isNull()) {
                if (advancedOperationKey.isPressed() && !AdvancedOperationHandler.isPressed()) {
                    AdvancedOperationHandler.press();
                } else if (AdvancedOperationHandler.isPressed()) {
                    AdvancedOperationHandler.release();
                }
            }
        });
    }
}
