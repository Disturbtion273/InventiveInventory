package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.handler.AdvancedOperationHandler;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {
    @Shadow
    @Final
    protected ScreenHandler handler;

    @Shadow
    protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Inject(method = "render", at = @At("HEAD"))
    private void mouseOverSlot(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        for (Slot slot : this.handler.slots) {
            if (this.isPointOverSlot(slot, mouseX, mouseY)) {
                MousePosition.setHoveredSlot(slot.id);
                break;
            } else {
                MousePosition.setHoveredSlot(-1);
            }
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        context.getMatrices().push();
        context.getMatrices().translate(this.x, this.y, 0.0f);

        List<Integer> lockedSlots = LockedSlotsHandler.get().adjust();
        for (Integer lockedSlot : lockedSlots) {
            Slot slot = SlotFinder.getSlotFromSlotIndex(lockedSlot);
            Drawer.drawSlotBorder(context, slot.x, slot.y, 0xFFFF0000);
        }
        context.getMatrices().pop();
    }

    @Inject(method = "drawSlotHighlight", at = @At("HEAD"), cancellable = true)
    private static void onDrawSlotHighlight(DrawContext context, int x, int y, int z, CallbackInfo ci) {
        Screen screen = InventiveInventory.getScreen();
        if (AdvancedOperationHandler.isPressed()) {
            if (screen instanceof InventoryScreen) {
                if (PlayerSlots.get(false).contains(SlotFinder.getSlotAtPosition(x, y).getIndex())) {
                    Drawer.drawSlotBackground(context, x, y, 0xFFFF8080);
                    ci.cancel();
                }
            }
        }
    }
}