package net.origins.inventive_inventory.features.locked_slots.mixins;

import net.fabricmc.fabric.mixin.renderer.client.SpriteAtlasTextureMixin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.texture.SpriteDimensions;
import net.minecraft.util.Identifier;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;

@Mixin(HandledScreen.class)
public class MixinLockedSlotsDrawer {
    @Shadow
    protected int x;

    @Shadow
    protected int y;

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        context.getMatrices().push();
        context.getMatrices().translate(this.x, this.y, 0.0f);

        MinecraftClient.getInstance().getTextureManager().bindTexture(Identifier.of(InventiveInventory.MOD_ID, "textures/gui/lock.png"));
        context.drawGuiTexture(Identifier.of(InventiveInventory.MOD_ID, "textures/gui/lock.png"), this.x, this.y, 50, 50);
//, "textures/gui/lock.png"


        List<Integer> lockedSlots = LockedSlotsHandler.getLockedSlots().adjust();

//        for (Integer lockedSlot : lockedSlots) {
//            Slot slot = SlotFinder.getSlotFromSlotIndex(lockedSlot);
//            Drawer.drawSlotBorder(context, slot.x, slot.y, 0xFFFF0000);
//        }
        context.getMatrices().pop();
    }

//    @Inject(method = "drawSlotHighlight", at = @At("HEAD"), cancellable = true)
//    private static void onDrawSlotHighlight(DrawContext context, int x, int y, int z, CallbackInfo ci) {
//        if (AdvancedOperationHandler.isPressed() && ScreenCheck.isPlayerInventory()) {
//            Slot slot = SlotFinder.getSlotAtPosition(x, y);
//            if (slot != null && PlayerSlots.get().contains(slot.getIndex())) {
//                Drawer.drawSlotBackground(context, x, y, 0xFFFF8080);
//                ci.cancel();
//            }
//        }
//    }
}
