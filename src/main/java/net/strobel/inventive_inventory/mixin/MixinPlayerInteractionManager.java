package net.strobel.inventive_inventory.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.strobel.inventive_inventory.event.DisconnectHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinPlayerInteractionManager {
    @Inject(method = "clickSlot", at = @At("HEAD"), cancellable = true)
    private void onClickSlot(int syncId, int slotId, int button, SlotActionType actionType, PlayerEntity player, CallbackInfo ci) {
        if (player.isCreative() && !DisconnectHandler.isDisconnecting()) {
            ScreenHandler screenHandler = player.currentScreenHandler;
            if (syncId != screenHandler.syncId) {
                return;
            }
            screenHandler.onSlotClick(slotId, button, actionType, player);
            ci.cancel();
        }
    }
}
