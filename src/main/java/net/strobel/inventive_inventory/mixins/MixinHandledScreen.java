package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.strobel.inventive_inventory.util.MousePosition;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {
    @Shadow @Final protected ScreenHandler handler;

    @Shadow protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Inject(method = "render", at = @At("HEAD"))
    private void mouseOverSlot(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for (Slot slot: this.handler.slots) {
            if (this.isPointOverSlot(slot, mouseX, mouseY)) {
                MousePosition.setHoveredSlot(slot.id);
                break;
            } else {
                MousePosition.setHoveredSlot(-1);
            }
        }
    }
}