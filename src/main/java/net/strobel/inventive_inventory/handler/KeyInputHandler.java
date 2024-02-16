package net.strobel.inventive_inventory.handler;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.util.ScreenCheck;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {
    public static final String INVENTIVE_INVENTORY_CATEGORY = "key.inventive_inventory.category.inventive_inventory";
    public static final String INVENTIVE_INVENTORY_PROFILES_CATEGORY = "key.inventive_inventory.category.inventive_inventory_profiles";
    private static final String KEY_ADVANCED_OPERATION = "key.inventive_inventory.advanced_operation";
    private static final String KEY_PROFILE_SAVING = "key.inventive_inventory.profile_saving";
    private static final String KEY_SORT_INVENTORY = "key.inventive_inventory.sort_inventory";
    public static KeyBinding advancedOperationKey;
    public static KeyBinding profileSavingKey;
    public static KeyBinding sortInventoryKey;

    public static KeyBinding[] profileKeys = new KeyBinding[9];
    private static final boolean[] executed = new boolean[9];


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
        profileSavingKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_PROFILE_SAVING,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_CONTROL,
                INVENTIVE_INVENTORY_PROFILES_CATEGORY
        ));

        for (int i = 0; i < 9; i++) {
            profileKeys[i] = (KeyBindingHelper.registerKeyBinding(new KeyBinding(
                    "key.inventive_inventory.profile_" + (i + 1),
                    InputUtil.Type.KEYSYM,
                    GLFW.GLFW_KEY_1 + i,
                    INVENTIVE_INVENTORY_PROFILES_CATEGORY
            )));
        }
    }

    public static void registerKeyInputs() {
        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (ScreenCheck.isNull()) {
                if (advancedOperationKey.isPressed() && !AdvancedOperationHandler.isPressed()) {
                    AdvancedOperationHandler.press();
                } else if (AdvancedOperationHandler.isPressed()) {
                    AdvancedOperationHandler.release();
                }
            }

            if (profileSavingKey.isPressed()) {
                for (int i = 0; i < profileKeys.length; i++) {
                    if (profileKeys[i].isPressed() && !executed[i]) {
                        KeyBinding keyBinding = profileKeys[i];
                        String name = Text.translatable(keyBinding.getTranslationKey()).getString();
                        if (!name.equals(keyBinding.getTranslationKey())) {
                            name = keyBinding.getTranslationKey();
                        }
                        ProfileHandler.save(name, keyBinding);
                        executed[i] = true;
                    } else if (!profileKeys[i].isPressed()) {
                        executed[i] = false;
                    }
                }
            }
        });
    }
}
