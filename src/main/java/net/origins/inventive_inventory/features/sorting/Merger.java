package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.sorting.SortingBehaviours;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.SlotRange;

import java.util.*;

public class Merger {

    public static void mergeItemStacks(SlotRange slotRange, ScreenHandler screenHandler) {
        ItemStack targetStack = InteractionHandler.getCursorStack().copy();
        generalMerge(slotRange, screenHandler);
        mergeCursorStack(slotRange, screenHandler);

//        if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.SORT_CURSOR_STACK) {
//            mergeCursorStack(slotRange, screenHandler);
//        } else if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.KEEP_CURSOR_STACK) {
//            adjustCursorStack(slotRange, screenHandler, targetStack);
//        } else if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.AOK_DEPENDENT) {
//            if (AdvancedOperationHandler.isPressed()) {
//                adjustCursorStack(slotRange, screenHandler, targetStack);
//            } else {
//                mergeCursorStack(slotRange, screenHandler);
//            }
//        } else if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.AOK_DEPENDENT_INVERTED) {
//            if (AdvancedOperationHandler.isPressed()) {
//                mergeCursorStack(slotRange, screenHandler);
//            } else {
//                adjustCursorStack(slotRange, screenHandler, targetStack);
//            }
//        }
    }

    private static void generalMerge(SlotRange slotRange, ScreenHandler screenHandler) {
        for (int slot : slotRange) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty() && stack.getCount() < stack.getMaxCount()) {
                InteractionHandler.leftClickStack(slot);
                for (int tempSlot = slot + 1; InteractionHandler.getCursorStack().getCount() < InteractionHandler.getCursorStack().getMaxCount()
                        && tempSlot <= slotRange.getLast() && !InteractionHandler.getCursorStack().isEmpty(); tempSlot++) {
                    if (ItemStack.areItemsEqual(InteractionHandler.getCursorStack(), screenHandler.getSlot(tempSlot).getStack())) {
                        InteractionHandler.leftClickStack(tempSlot);
                    }
                }
                InteractionHandler.leftClickStack(slot);
            }
        }
    }

    private static void mergeCursorStack(SlotRange slotRange, ScreenHandler screenHandler) {
        Integer emptySlot = null;

        for (int slot : slotRange) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (ItemStack.areItemsEqual(InteractionHandler.getCursorStack(), stack)) {
                InteractionHandler.leftClickStack(slot);
            } else if (stack.isEmpty() && emptySlot == null) {
                emptySlot = slot;
            }
        }
        if (!InteractionHandler.isCursorEmpty() && emptySlot != null) InteractionHandler.leftClickStack(emptySlot);
    }

    public static void adjustCursorStack(SlotRange slotRange, ScreenHandler screenHandler, ItemStack targetStack) {
        if (targetStack.getCount() < InteractionHandler.getCursorStack().getCount()) {
            decreaseCursorStack(slotRange, screenHandler, targetStack);
        } else if (targetStack.getCount() > InteractionHandler.getCursorStack().getCount()) {
            increaseCursorStack(slotRange, screenHandler, targetStack);
        }
    }

    private static void decreaseCursorStack(SlotRange slotRange, ScreenHandler screenHandler, ItemStack targetStack) {
        Integer emptySlot = null;

        for (int slot : slotRange) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (ItemStack.areItemsEqual(stack, InteractionHandler.getCursorStack()) && stack.getCount() < stack.getMaxCount()) {
                while (stack.getCount() < stack.getMaxCount() && targetStack.getCount() < InteractionHandler.getCursorStack().getCount()) {
                    InteractionHandler.rightClickStack(slot);
                    stack = screenHandler.getSlot(slot).getStack();
                }
                if (targetStack.getCount() == InteractionHandler.getCursorStack().getCount()) return;
            } else if (stack.isEmpty()) {
                emptySlot = slot;
            }
        }

        while (targetStack.getCount() < InteractionHandler.getCursorStack().getCount() && emptySlot != null) {
            InteractionHandler.rightClickStack(emptySlot);
        }
    }

    private static void increaseCursorStack(SlotRange slotRange, ScreenHandler screenHandler, ItemStack targetStack) {
        for (int slot : slotRange) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!ItemStack.areItemsEqual(stack, InteractionHandler.getCursorStack())) {
                InteractionHandler.leftClickStack(slot);
                Map<Integer, ItemStack> sameStacks = new HashMap<>();
                for (int tempSlot : slotRange.copy().exclude(slot)) {
                    ItemStack tempStack = screenHandler.getSlot(tempSlot).getStack();
                    if (ItemStack.areItemsEqual(tempStack, targetStack)) {
                        sameStacks.put(tempSlot, tempStack);
                    }
                }

                while (targetStack.getCount() != screenHandler.getSlot(slot).getStack().getCount() && !sameStacks.isEmpty()) {
                    Integer minStack = Collections.min(sameStacks.entrySet(), Comparator.comparingInt(e -> e.getValue().getCount())).getKey();
                    sameStacks.remove(minStack);
                    InteractionHandler.leftClickStack(minStack);
                    while (targetStack.getCount() > screenHandler.getSlot(slot).getStack().getCount() && !InteractionHandler.isCursorEmpty()) {
                        InteractionHandler.rightClickStack(slot);
                    }
                    if (targetStack.getCount() == screenHandler.getSlot(slot).getStack().getCount()) {
                        InteractionHandler.leftClickStack(minStack);
                        InteractionHandler.leftClickStack(slot);
                        return;
                    }
                }
            }
        }
    }
}
