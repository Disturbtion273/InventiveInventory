package net.strobel.inventive_inventory.keybindfix.mixins;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.handler.AdvancedOperationHandler;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.keybindfix.IKeyBindingDisplay;
import net.strobel.inventive_inventory.keybindfix.KeybindFixer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(value = KeyBinding.class, priority = 10000)
public abstract class MixinKeyBinding implements IKeyBindingDisplay {
    @Unique
    private String displayName;
    @Final
    @Shadow
    private static Map<String, KeyBinding> KEYS_BY_ID;
    @Final
    @Shadow
    private static Map<InputUtil.Key, KeyBinding> KEY_TO_BINDINGS;
    @Shadow
    private InputUtil.Key boundKey;
    @Unique
    private static final KeybindFixer keybindFixer = new KeybindFixer();
    @Final
    @Mutable
    @Shadow
    private String translationKey;

    @Inject(method = "onKeyPressed", at = @At(value = "HEAD"), cancellable = true)
    private static void onKeyPressedFixed(InputUtil.Key key, CallbackInfo ci) {
        if (keybindFixer.checkHotbarKeys(key)) ci.cancel();
    }

    @Inject(method = "onKeyPressed", at = @At(value = "TAIL"))
    private static void onKeyPressedFixed(InputUtil.Key key, CallbackInfo ci, @Local KeyBinding original) {
        keybindFixer.onKeyPressed(key, original, KEY_TO_BINDINGS.get(key));
    }

    @Inject(method = "setKeyPressed", at = @At(value = "TAIL"))
    private static void setKeyPressedFixed(InputUtil.Key key, boolean pressed, CallbackInfo ci, @Local KeyBinding original) {
        keybindFixer.setKeyPressed(key, pressed, original, KEY_TO_BINDINGS.get(key));
    }

    @Inject(method = "updateKeysByCode", at = @At(value = "TAIL"))
    private static void updateByCodeToMultiMap(CallbackInfo ci) {
        keybindFixer.clearMap();
        for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
            keybindFixer.putKey(((MixinIKeyBindingAccessor) keyBinding).getBoundKey(), keyBinding);
        }
    }

    @Inject(method = "<init>(Ljava/lang/String;Lnet/minecraft/client/util/InputUtil$Type;ILjava/lang/String;)V", at = @At(value = "TAIL"))
    private void putToMultiMap(String translationKey, InputUtil.Type type, int code, String category, CallbackInfo ci) {
        keybindFixer.putKey(boundKey, (KeyBinding) (Object) this);
    }

    @Inject(method = "getTranslationKey", at = @At("HEAD"), cancellable = true)
    private void onGetTranslationKey(CallbackInfoReturnable<String> cir) {
        List<KeyBinding> profileKeys = Arrays.asList(KeyInputHandler.profileKeys);
        if (profileKeys.contains((KeyBinding) (Object) this)) {
            if (this.displayName != null) {
                cir.setReturnValue(this.displayName);
                cir.cancel();
            }
        }
    }

    @Inject(method = "setBoundKey", at = @At("TAIL"))
    private void onSetBoundKey(InputUtil.Key boundKey, CallbackInfo ci) {
        if ((Object) this == KeyInputHandler.advancedOperationKey) {
            AdvancedOperationHandler.setBoundKey(KeyBindingHelper.getBoundKeyOf(KeyInputHandler.advancedOperationKey));
        }
    }

    @Override
    public String main$getDisplayName() {
        if (this.displayName == null) {
            this.displayName = Text.translatable(this.translationKey).getString();
            return this.displayName;
        }
        return this.displayName.replaceFirst("Profile: ", "");
    }

    @Override
    public void main$setDisplayName(String displayName) {
        if (Text.translatable(this.translationKey).getString().equals(displayName)) {
            this.displayName = displayName;
            return;
        }
        this.displayName = "Profile: " + displayName;
    }

    @Override
    public void main$resetDisplayName() {
        this.displayName = null;
    }
}