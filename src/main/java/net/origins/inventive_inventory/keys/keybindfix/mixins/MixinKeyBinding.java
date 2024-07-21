package net.origins.inventive_inventory.keys.keybindfix.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.origins.inventive_inventory.keys.keybindfix.KeybindFixer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = KeyBinding.class, priority = 10000)
public abstract class MixinKeyBinding {
    @Final @Shadow private static Map<String, KeyBinding> KEYS_BY_ID;
    @Final @Shadow private static Map<InputUtil.Key, KeyBinding> KEY_TO_BINDINGS;
    @Shadow private InputUtil.Key boundKey;

    @Inject(method = "onKeyPressed", at = @At(value = "HEAD"), cancellable = true)
    private static void checkForHotbarKeys(InputUtil.Key key, CallbackInfo ci) {
        if (KeybindFixer.checkHotbarKeys(key)) ci.cancel();
    }

    @Inject(method = "onKeyPressed", at = @At(value = "TAIL"))
    private static void onKeyPressedFixed(InputUtil.Key key, CallbackInfo ci, @Local KeyBinding original) {
        KeybindFixer.onKeyPressed(key, original, KEY_TO_BINDINGS.get(key));
    }

    @Inject(method = "setKeyPressed", at = @At(value = "TAIL"))
    private static void setKeyPressedFixed(InputUtil.Key key, boolean pressed, CallbackInfo ci, @Local KeyBinding original) {
        KeybindFixer.setKeyPressed(key, pressed, original, KEY_TO_BINDINGS.get(key));
    }

    @Inject(method = "updateKeysByCode", at = @At(value = "TAIL"))
    private static void updateByCodeToMultiMap(CallbackInfo ci) {
        KeybindFixer.clearMap();
        for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
            KeybindFixer.putKey(KeyBindingHelper.getBoundKeyOf(keyBinding), keyBinding);
        }
    }

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At(value = "TAIL"))
    private void putToMultiMap(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo ci) {
        KeybindFixer.putKey(boundKey, (KeyBinding) (Object) this);
    }
}