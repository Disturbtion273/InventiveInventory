package net.strobel.inventive_inventory.handler;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_INVENTIVE_INVENTORY = "key.inventive_inventory.category.inventive_inventory";
    public static final String ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";
    public static final String KEY_SORT_INVENTORY = "key.inventive_inventory.sort_inventory";
    public static KeyBinding advancedOperationKey;
    public static KeyBinding sortInventoryKey;


    public static void register() {
        advancedOperationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                ADVANCED_OPERATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                KEY_CATEGORY_INVENTIVE_INVENTORY
        ));
        sortInventoryKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SORT_INVENTORY,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY_INVENTIVE_INVENTORY
        ));
    }

    public static void registerKeyInputs() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (advancedOperationKey.isPressed()) {
                AdvancedOperationHandler.press();
            }
        });
    }
}
