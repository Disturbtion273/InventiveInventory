package net.origins.inventive_inventory.keys;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyRegistry {
    public static final String INVENTIVE_INVENTORY_CATEGORY = "key.inventive_inventory.category.inventive_inventory";
    private static final String KEY_SORT = "key.inventive_inventory.sort";
    public static KeyBinding sortKey;

    public static void register() {
        sortKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SORT,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_R,
                INVENTIVE_INVENTORY_CATEGORY
        ));
    }
}
