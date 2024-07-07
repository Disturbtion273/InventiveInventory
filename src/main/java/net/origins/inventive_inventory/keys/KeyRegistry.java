package net.origins.inventive_inventory.keys;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyRegistry {
    public static final String INVENTIVE_INVENTORY_CATEGORY = "key.inventive_inventory.category.inventive_inventory";
    private static final String KEY_SORT = "key.inventive_inventory.sort";
    private static final String KEY_ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";
    private static final String OPEN_PROFILES_SCREEN = "key.inventive_inventory.open_profiles_screen";

    public static KeyBinding sortKey;
    public static KeyBinding advancedOperationKey;
    public static KeyBinding openProfilesScreenKey;

    public static void register() {
        sortKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_SORT,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_R,
                INVENTIVE_INVENTORY_CATEGORY
        ));
        advancedOperationKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_ADVANCED_OPERATION,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_LEFT_ALT,
                INVENTIVE_INVENTORY_CATEGORY
        ));
        openProfilesScreenKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                OPEN_PROFILES_SCREEN,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_V,
                INVENTIVE_INVENTORY_CATEGORY
        ));
    }
}
