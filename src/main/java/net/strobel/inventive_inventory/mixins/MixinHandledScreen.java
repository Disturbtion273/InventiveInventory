package net.strobel.inventive_inventory.mixins;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlots;
import net.strobel.inventive_inventory.handler.AdvancedOperationHandler;
import net.strobel.inventive_inventory.util.Drawer;
import net.strobel.inventive_inventory.util.FileHandler;
import net.strobel.inventive_inventory.util.MousePosition;
import net.strobel.inventive_inventory.util.SlotFinder;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class MixinHandledScreen {
    @Shadow
    @Final
    protected ScreenHandler handler;

    @Shadow
    protected abstract boolean isPointOverSlot(Slot slot, double pointX, double pointY);

    @Shadow protected int x;

    @Shadow protected int y;

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
        if (InventiveInventoryClient.getScreenHandler() instanceof PlayerScreenHandler) {
            context.getMatrices().push();
            context.getMatrices().translate(this.x, this.y, 0.0f);
            JsonArray locked_slots = FileHandler.get(LockedSlots.LOCKED_SLOTS_PATH);
            for (JsonElement locked_slot : locked_slots) {
                Slot slot = SlotFinder.getSlotFromSlotIndex(locked_slot.getAsInt());
                Drawer.drawSlotBorder(context, slot.x, slot.y, 0xFFFF0000);
            }
            context.getMatrices().pop();
        }
    }

    @Inject(method = "drawSlotHighlight", at = @At("HEAD"), cancellable = true)
    private static void onDrawSlotHighlight(DrawContext context, int x, int y, int z, CallbackInfo ci) {
        if (AdvancedOperationHandler.isPressed()) {
            Drawer.drawSlotBorder(context, x, y, 0xFFFF0000);
            ci.cancel();
        }
    }
}