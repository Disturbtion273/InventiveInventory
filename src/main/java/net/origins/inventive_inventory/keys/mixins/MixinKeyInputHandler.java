package net.origins.inventive_inventory.keys.mixins;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.origins.inventive_inventory.context.ContextManager;
import net.origins.inventive_inventory.context.Contexts;
import net.origins.inventive_inventory.features.sorting.SortingHandler;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public class MixinKeyInputHandler {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (KeyRegistry.advancedOperationKey.matchesKey(keyCode, scanCode)) {
            AdvancedOperationHandler.setPressed(true);
        }
        if (KeyRegistry.sortKey.matchesKey(keyCode, scanCode) && ContextManager.isInit()) {
            ContextManager.setContext(Contexts.SORTING);
            SortingHandler.sort();
            ContextManager.setContext(Contexts.INIT);
        }
    }

    @Inject(method = "onMouseClick(I)V", at = @At("HEAD"))
    private void onMouseClick(int button, CallbackInfo ci) {
        if (KeyRegistry.advancedOperationKey.matchesMouse(button)) {
            AdvancedOperationHandler.setPressed(true);
        }
        if (KeyRegistry.sortKey.matchesMouse(button) && ContextManager.isInit()) {
            ContextManager.setContext(Contexts.SORTING);
            SortingHandler.sort();
            ContextManager.setContext(Contexts.INIT);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (AdvancedOperationHandler.isReleased()) {
            AdvancedOperationHandler.setPressed(false);
        }
    }
}
