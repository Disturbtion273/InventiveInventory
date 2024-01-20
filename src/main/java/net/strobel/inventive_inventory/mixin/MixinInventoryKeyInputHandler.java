package net.strobel.inventive_inventory.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.event.KeyInputHandler;
import net.strobel.inventive_inventory.features.sorting.Sorter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Screen.class)
public class MixinInventoryKeyInputHandler {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof AbstractInventoryScreen<?>) {
            if (KeyInputHandler.sortInventoryKey.matchesKey(keyCode, scanCode)) {
                Sorter.sortPlayerInventory();
            }
        } else if ((Object) this instanceof GenericContainerScreen) {
            if (KeyInputHandler.sortInventoryKey.matchesKey(keyCode, scanCode)) {
                Sorter.sortContainerInventory();
            }
        } else if ((Object) this instanceof HopperScreen) {
            if (KeyInputHandler.sortInventoryKey.matchesKey(keyCode, scanCode)) {
                Sorter.sortHopperInventory();
            }
        } else if ((Object) this instanceof Generic3x3ContainerScreen) {
            if (KeyInputHandler.sortInventoryKey.matchesKey(keyCode, scanCode)) {
                Sorter.sort3x3Container();
            }
        }

    }
}