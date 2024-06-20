package net.origins.inventive_inventory.keys.mixins;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.origins.inventive_inventory.features.sorting.SortingHandler;
import net.origins.inventive_inventory.keys.KeyRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class MixinKeyInputHandler {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (KeyRegistry.sortKey.matchesKey(keyCode, scanCode)) {
            SortingHandler.sort();
        }
    }
}
