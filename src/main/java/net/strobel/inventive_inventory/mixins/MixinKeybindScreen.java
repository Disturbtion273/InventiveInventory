package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeybindsScreen.class)
public abstract class MixinKeybindScreen {
    @Inject(method = "init", at = @At("HEAD"))
    private void init(CallbackInfo ci) {
        ProfileHandler.initialize();
    }
}
