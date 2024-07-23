package net.origins.inventive_inventory.features.locked_slots.mixins;

import net.minecraft.entity.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemEntity.class)
public class MixinItemPickup {
//    @Unique
//    private static List<ItemStack> mainInventory;

//    @Inject(method = "onPlayerCollision", at = @At("HEAD"))
//    private void beforePlayerCollision(PlayerEntity player, CallbackInfo ci) {
//        mainInventory = player.getInventory().main.stream().toList();
//    }
//
//    @Inject(method = "onPlayerCollision", at = @At("RETURN"))
//    private void afterPlayerCollision(PlayerEntity player, CallbackInfo ci) {
//        for (int slot : LockedSlotsHandler.getLockedSlots()) {
//            if (!player.getInventory().main.get(slot).equals(mainInventory.get(slot))) {
//                for (int freeSlot : PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT)) {
//                    if (player.getInventory().main.get(freeSlot).isEmpty()) {
//                        InteractionHandler.swapStacks(slot, freeSlot);
//                        return;
//                    }
//                }
//                System.out.println("Full Inventory");
//            }
//        }
//    }
}
