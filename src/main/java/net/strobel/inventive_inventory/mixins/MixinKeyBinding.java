package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.keybindfix.MixinIKeyBindingDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

import static net.strobel.inventive_inventory.handler.KeyInputHandler.profileKeys;

@Mixin(KeyBinding.class)
public abstract class MixinKeyBinding {

    @Inject(method = "setBoundKey", at = @At("HEAD"))
    private void setBoundKey(InputUtil.Key boundKey, CallbackInfo ci) {
        if (Arrays.stream(profileKeys).toList().contains(this)) {
            String name = ((MixinIKeyBindingDisplay) this).main$getDisplayName();
            ProfileHandler.overwrite(name, boundKey.getLocalizedText().getString());
        }
    }
}
