package net.origins.inventive_inventory.features.locked_slots.mixins;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.collection.DefaultedList;
import net.origins.inventive_inventory.features.locked_slots.LockedSlotsHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.SERVER)
@Mixin(ScreenHandler.class)
public class MixinItemQuickMove {
    @Shadow @Final public DefaultedList<Slot> slots;

    @Inject(method = "insertItem", at = @At("HEAD"), cancellable = true)
    private void onInsertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast, CallbackInfoReturnable<Boolean> cir) {
        boolean bl = false;
        int i = startIndex;
        if (fromLast) {
            i = endIndex - 1;
        }

        Slot slot;
        ItemStack itemStack;
        int j;
        if (stack.isStackable()) {
            while(!stack.isEmpty()) {
                if (fromLast) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }
                if (LockedSlotsHandler.getLockedSlots().adjust().contains(i)) {
                    if (fromLast) --i;
                    else ++i;
                    continue;
                }

                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (!itemStack.isEmpty() && ItemStack.areItemsAndComponentsEqual(stack, itemStack)) {
                    j = itemStack.getCount() + stack.getCount();
                    int k = slot.getMaxItemCount(itemStack);
                    if (j <= k) {
                        stack.setCount(0);
                        itemStack.setCount(j);
                        slot.markDirty();
                        bl = true;
                    } else if (itemStack.getCount() < k) {
                        stack.decrement(k - itemStack.getCount());
                        itemStack.setCount(k);
                        slot.markDirty();
                        bl = true;
                    }
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        if (!stack.isEmpty()) {
            if (fromLast) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while(true) {
                if (fromLast) {
                    if (i < startIndex) {
                        break;
                    }
                } else if (i >= endIndex) {
                    break;
                }
                if (LockedSlotsHandler.getLockedSlots().adjust().contains(i)) {
                    if (fromLast) --i;
                    else ++i;
                    continue;
                }

                slot = this.slots.get(i);
                itemStack = slot.getStack();
                if (itemStack.isEmpty() && slot.canInsert(stack)) {
                    j = slot.getMaxItemCount(stack);
                    slot.setStack(stack.split(Math.min(stack.getCount(), j)));
                    slot.markDirty();
                    bl = true;
                    break;
                }

                if (fromLast) {
                    --i;
                } else {
                    ++i;
                }
            }
        }

        cir.setReturnValue(bl);
    }
}
