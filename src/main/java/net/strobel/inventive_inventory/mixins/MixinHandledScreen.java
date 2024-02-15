package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.handler.AdvancedOperationHandler;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.Drawer;
import net.strobel.inventive_inventory.util.MousePosition;
import net.strobel.inventive_inventory.util.ScreenCheck;
import net.strobel.inventive_inventory.util.SlotFinder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {

    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Shadow
    @Nullable
    protected Slot focusedSlot;

    @Inject(method = "render", at = @At("HEAD"))
    private void mouseOverSlot(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (this.focusedSlot != null) {
            MousePosition.setHoveredSlot(this.focusedSlot.id);
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        context.getMatrices().push();
        context.getMatrices().translate(this.x, this.y, 0.0f);

        List<Integer> lockedSlots = LockedSlotsHandler.getLockedSlots().adjust();
        for (Integer lockedSlot : lockedSlots) {
            Slot slot = SlotFinder.getSlotFromSlotIndex(lockedSlot);
            Drawer.drawSlotBorder(context, slot.x, slot.y, 0xFFFF0000);
        }
        context.getMatrices().pop();
    }

    @Inject(method = "drawSlotHighlight", at = @At("HEAD"), cancellable = true)
    private static void onDrawSlotHighlight(DrawContext context, int x, int y, int z, CallbackInfo ci) {
        if (AdvancedOperationHandler.isPressed() && ScreenCheck.isPlayerInventory()) {
            Slot slot = SlotFinder.getSlotAtPosition(x, y);
            if (slot != null && PlayerSlots.get().contains(slot.getIndex())) {
                Drawer.drawSlotBackground(context, x, y, 0xFFFF8080);
                ci.cancel();
            }
        }
    }
}