package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.strobel.inventive_inventory.handler.AdvancedOperationHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public abstract class MixinClientPlayerInteractionManager {
    @Shadow
    public abstract void clickCreativeStack(ItemStack stack, int slotId);

    @Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
    private void onClickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (AdvancedOperationHandler.isPressed() && actionType == SlotActionType.PICKUP) {
            if (InventiveInventory.getScreen() instanceof InventoryScreen) {
                LockedSlotsHandler.toggle();
                ci.cancel();
            }
        } else {
            if (player.isCreative() && InventiveInventory.getClient().currentScreen instanceof CreativeInventoryScreen) {
                clickCreativeStack(InventiveInventory.getScreenHandler().getSlot(slotId).getStack(), slotId);
            }
        }
    }
}

