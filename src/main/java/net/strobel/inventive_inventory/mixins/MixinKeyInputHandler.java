package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.screen.PlayerScreenHandler;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlots;
import net.strobel.inventive_inventory.features.sorting.Sorter;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.util.Arrays;

@Mixin(Screen.class)
public class MixinKeyInputHandler {
    @Inject(method = "keyPressed", at = @At("HEAD"))
    private void onKeyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) throws IOException {
        if (KeyInputHandler.sortInventoryKey.matchesKey(keyCode, scanCode)) {
            if ((Object) this instanceof HandledScreen<?>) {
                Sorter.sort();
            }
        } else if (KeyInputHandler.lockSlotKey.matchesKey(keyCode, scanCode)) {
            if (InventiveInventoryClient.getScreenHandler() instanceof PlayerScreenHandler) {
                LockedSlots.set();
                LockedSlots.print();
            }
        }
    }
}
