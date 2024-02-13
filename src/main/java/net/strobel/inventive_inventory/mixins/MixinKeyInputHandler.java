package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.features.profiles.Profile;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.handler.AdvancedOperationHandler;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.features.sorting.Sorter;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class MixinKeyInputHandler {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (InventiveInventory.getPlayer() != null) {
            if (KeyInputHandler.advancedOperationKey.matchesKey(keyCode, scanCode)) {
                AdvancedOperationHandler.press();
                ProfileHandler.save(GLFW.GLFW_KEY_1);
            }
            if (KeyInputHandler.sortInventoryKey.matchesKey(keyCode, scanCode)) {
                if ((Object) this instanceof HandledScreen<?>) {
                    Sorter.sort();
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (AdvancedOperationHandler.isReleased()) {
            AdvancedOperationHandler.release();
        }
    }
}
