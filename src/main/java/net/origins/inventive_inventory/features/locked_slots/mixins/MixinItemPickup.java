package net.origins.inventive_inventory.features.locked_slots.mixins;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(PlayerInventory.class)
public abstract class MixinItemPickup {

    @Shadow protected abstract boolean canStackAddMore(ItemStack existingStack, ItemStack stack);

    @Shadow public abstract ItemStack getStack(int slot);

    @Shadow public int selectedSlot;

    @Shadow @Final public DefaultedList<ItemStack> main;

    @Inject(method = "getOccupiedSlotWithRoomForStack", at = @At("HEAD"), cancellable = true)
    private void onGetOccupiedSlotWithRoomForStack(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (this.canStackAddMore(this.getStack(this.selectedSlot), stack)) {
            cir.setReturnValue(this.selectedSlot);
            return;
        }
        if (this.canStackAddMore(this.getStack(40), stack)) {
            cir.setReturnValue(40);
            return;
        }
        List<Integer> lockedSlots = LockedSlotsHandler.getLockedSlots();
        for (int i = 0; i < this.main.size(); ++i) {
            if (!this.canStackAddMore(this.main.get(i), stack) || lockedSlots.contains(i)) continue;
            cir.setReturnValue(i);
            return;
        }
        cir.setReturnValue(-1);
    }

    @Inject(method = "getEmptySlot", at = @At("HEAD"), cancellable = true)
    private void onGetEmptySlot(CallbackInfoReturnable<Integer> cir) {
        List<Integer> lockedSlots = LockedSlotsHandler.getLockedSlots();
        for (int i = 0; i < this.main.size(); ++i) {
            if (!this.main.get(i).isEmpty() || lockedSlots.contains(i)) continue;
            cir.setReturnValue(i);
            return;
        }
        cir.setReturnValue(-1);
    }
}
