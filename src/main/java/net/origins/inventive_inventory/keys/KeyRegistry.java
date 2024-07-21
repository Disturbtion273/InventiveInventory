package net.origins.inventive_inventory.keys;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyRegistry {
    public static final String INVENTIVE_INVENTORY_CATEGORY = "key.inventive_inventory.category.inventive_inventory";
    public static final String INVENTIVE_INVENTORY_PROFILES_CATEGORY = "key.inventive_inventory.category.inventive_inventory_profiles";
    private static final String KEY_SORT = "key.inventive_inventory.sort";
    private static final String KEY_ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";
    private static final String KEY_OPEN_PROFILES_SCREEN = "key.inventive_inventory.open_profiles_screen";
    private static final String KEY_LOAD_PROFILE = "key.inventive_inventory.load_profile";

    public static KeyBinding sortKey;
    public static KeyBinding advancedOperationKey;
    public static KeyBinding openProfilesScreenKey;
    public static KeyBinding loadProfileKey;
    public static KeyBinding[] profileKeys = new KeyBinding[3];

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
                KEY_OPEN_PROFILES_SCREEN,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_V,
                INVENTIVE_INVENTORY_PROFILES_CATEGORY
        ));
        loadProfileKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_LOAD_PROFILE,
                InputUtil.Type.KEYSYM,
                InputUtil.GLFW_KEY_LEFT_ALT,
                INVENTIVE_INVENTORY_PROFILES_CATEGORY
        ));
        for (int i = 0; i < profileKeys.length; i++) {
            profileKeys[i] = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.inventive_inventory.profile_" + i,
                    InputUtil.Type.KEYSYM,
                    InputUtil.GLFW_KEY_1 + i,
                    INVENTIVE_INVENTORY_PROFILES_CATEGORY
            ));
        }
    }
}
