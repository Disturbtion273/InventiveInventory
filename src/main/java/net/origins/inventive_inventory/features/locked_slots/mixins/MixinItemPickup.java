package net.origins.inventive_inventory.features.locked_slots.mixins;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(ItemEntity.class)
public class MixinItemPickup {
    @Unique
    private static List<ItemStack> mainInventory;

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
