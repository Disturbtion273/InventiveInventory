package net.strobel.inventive_inventory.mixins;

import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import net.strobel.inventive_inventory.InventiveInventoryClient;
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
        if (AdvancedOperationHandler.isPressed()) {
            if (InventiveInventoryClient.getScreen() instanceof InventoryScreen) {
                LockedSlotsHandler.toggle();
                ci.cancel();
            }
        } else {
            if (player.isCreative() && InventiveInventoryClient.getClient().currentScreen instanceof CreativeInventoryScreen) {
                clickCreativeStack(InventiveInventoryClient.getScreenHandler().getSlot(slotId).getStack(), slotId);
            }
        }
    }
}

