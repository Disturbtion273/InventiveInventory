package net.origins.inventive_inventory.features.locked_slots.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.Drawer;
import net.origins.inventive_inventory.util.ScreenCheck;
import net.origins.inventive_inventory.util.Textures;
import net.origins.inventive_inventory.util.mouse.MouseLocation;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HandledScreen.class)
public class MixinLockedSlotsDrawer {
    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!InventiveInventory.getPlayer().isInCreativeMode()) {
            context.getMatrices().push();
            context.getMatrices().translate(this.x, this.y, 0.0f);

            List<Integer> lockedSlots = LockedSlotsHandler.getLockedSlots().adjust();
            for (Integer lockedSlot : lockedSlots) {
                Slot slot = InventiveInventory.getScreenHandler().getSlot(lockedSlot);
                Drawer.drawSlotBackground(context, slot.x, slot.y, 0xFF4D4D4D, 0);
                context.drawTexture(Textures.LOCK, slot.x + 11, slot.y - 2, 300, 0, 0, 8, 8, 8, 8);
            }

            context.getMatrices().pop();
        }
    }

    @Inject(method = "drawSlotHighlight", at = @At("HEAD"), cancellable = true)
    private static void onDrawSlotHighlight(DrawContext context, int x, int y, int z, CallbackInfo ci) {
        if (!InventiveInventory.getPlayer().isInCreativeMode()) {
            if (AdvancedOperationHandler.isPressed() && ScreenCheck.isPlayerInventory()) {
                Slot slot = MouseLocation.getHoveredSlot();
                if (slot != null) {
                    if (LockedSlotsHandler.getLockedSlots().contains(slot.getIndex())) {
                        Drawer.drawSlotBackground(context, x, y, 0xFF8B0000, 1);
                        ci.cancel();
                    } else if (PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT).contains(slot.getIndex())) {
                        Drawer.drawSlotBackground(context, x, y, 0x66FF0000, 1);
                        ci.cancel();
                    }
                }
            }
        }
    }
}
