package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.strobel.inventive_inventory.handler.AdvancedOperationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyBinding.class)
public class MixinKeyBinding {
    @Inject(method = "setBoundKey", at = @At("TAIL"))
    private void onSetBoundKey(InputUtil.Key boundKey, CallbackInfo ci) {
        AdvancedOperationHandler.setBoundKey(boundKey);
    }
}
