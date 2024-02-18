package net.strobel.inventive_inventory.keybindfix;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.keybindfix.mixins.MixinIKeyBindingAccessor;

import java.util.Arrays;
import java.util.List;

public class KeybindFixer {

    private final Multimap<InputUtil.Key, KeyBinding> keyFixMap = ArrayListMultimap.create();

    public void putKey(InputUtil.Key key, KeyBinding keyBinding) {
        keyFixMap.put(key, keyBinding);
    }

    public void clearMap() {
        keyFixMap.clear();
    }

    public boolean checkHotbarKeys(InputUtil.Key key) {
        List<KeyBinding> allHotbarKeyBindings = Arrays.stream(InventiveInventory.getClient().options.hotbarKeys).toList();
        List<KeyBinding> keyBindings = keyFixMap.get(key).stream().toList();
        for (KeyBinding keyBind : keyBindings) {
            if (allHotbarKeyBindings.contains(keyBind)) {
                if (KeyInputHandler.profileSavingKey.isPressed() || KeyInputHandler.profileLoadingKey.isPressed()) {
                    return true;
                }
            }
        }
        return false;
    }

    public void onKeyPressed(InputUtil.Key key, KeyBinding finalBinding, KeyBinding baseBinding) {
        if (finalBinding != baseBinding) return;
        for (KeyBinding keyBinding : keyFixMap.get(key)) {
            if (keyBinding == null || keyBinding == baseBinding) continue;
            MixinIKeyBindingAccessor keyBindingAccessor  = (MixinIKeyBindingAccessor) keyBinding;
            keyBindingAccessor.setTimesPressed(keyBindingAccessor.getTimesPressed() + 1);
        }
    }

    public void setKeyPressed(InputUtil.Key key, boolean pressed, KeyBinding finalBinding, KeyBinding baseBinding) {
        if (finalBinding != baseBinding) return;
        for (KeyBinding keyBinding : keyFixMap.get(key)) {
            if (keyBinding == null || keyBinding == baseBinding) continue;
            keyBinding.setPressed(pressed);
        }
    }
}
