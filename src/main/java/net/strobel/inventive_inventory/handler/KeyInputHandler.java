package net.strobel.inventive_inventory.handler;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String KEY_CATEGORY_INVENTIVE_INVENTORY = "key.inventive_inventory.category.inventive_inventory";
    public static final String KEY_SORT_INVENTORY = "key.inventive_inventory.sort_inventory";
    public static final String LOCK_SLOT = "key.inventive_inventory.lock_slot";
    public static KeyBinding sortInventoryKey;
    public static KeyBinding lockSlotKey;


    public static void register() {
        sortInventoryKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SORT_INVENTORY,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                KEY_CATEGORY_INVENTIVE_INVENTORY
        ));
        lockSlotKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                LOCK_SLOT,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                KEY_CATEGORY_INVENTIVE_INVENTORY
        ));
    }
}
