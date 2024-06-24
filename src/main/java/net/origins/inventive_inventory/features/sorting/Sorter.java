package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.sorting.SortingBehaviours;
import net.origins.inventive_inventory.config.sorting.SortingModes;
import net.origins.inventive_inventory.keys.handler.AdvancedOperationHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.SlotRange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sorter {

    public static void sortItemStacks(SlotRange slotRange, ItemStack targetStack) {
        List<Integer> sortedSlots = new ArrayList<>(getSortedSlots(slotRange));
        for (int i = 0; i < sortedSlots.size(); i++) {
            if (sortedSlots.get(i).intValue() == slotRange.get(i).intValue()) continue;
            if (ItemStack.areItemsEqual(InteractionHandler.getStackFromSlot(sortedSlots.get(i)), InteractionHandler.getCursorStack())) {
                for (int slot : slotRange) {
                    if (!ItemStack.areItemsEqual(InteractionHandler.getStackFromSlot(slot), InteractionHandler.getCursorStack())) {
                        InteractionHandler.leftClickStack(slot);
                        InteractionHandler.swapStacks(sortedSlots.get(i), slotRange.get(i));
                        InteractionHandler.leftClickStack(slot);
                        break;
                    }
                }
            } else {
                InteractionHandler.swapStacks(sortedSlots.get(i), slotRange.get(i));

            }
            if (sortedSlots.contains(slotRange.get(i))) {
                sortedSlots.set(sortedSlots.indexOf(slotRange.get(i)), sortedSlots.get(i));
            }
            sortedSlots.set(i, slotRange.get(i));
        }
        if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.KEEP_CURSOR_STACK) {
            adjustCursorStack(slotRange, targetStack);
        } else if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.AOK_DEPENDENT && AdvancedOperationHandler.isPressed()) {
            adjustCursorStack(slotRange, targetStack);
        } else if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.AOK_DEPENDENT_INVERTED) {
            adjustCursorStack(slotRange, targetStack);
        }
    }

    private static List<Integer> getSortedSlots(SlotRange slotRange) {
        if (ConfigManager.SORTING_MODE == SortingModes.NAME) {
            return slotRange.stream()
                    .filter(slot -> !InteractionHandler.getStackFromSlot(slot).isEmpty())
                    .sorted(Comparator.comparing((Integer slot) -> InteractionHandler.getStackFromSlot(slot).getName().getString())
                            .thenComparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                    .toList();
        } else {
            return slotRange.stream()
                    .filter(slot -> !InteractionHandler.getStackFromSlot(slot).isEmpty())
                    .sorted(Comparator.comparing((Integer slot) -> Item.getRawId(InteractionHandler.getStackFromSlot(slot).getItem()))
                            .thenComparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                    .toList();
        }
    }

    private static void adjustCursorStack(SlotRange slotRange, ItemStack targetStack) {
        if (targetStack.getCount() > InteractionHandler.getCursorStack().getCount()) {
            increaseCursorStack(slotRange, targetStack);
        }
    }

    private static void increaseCursorStack(SlotRange slotRange, ItemStack targetStack) {
        for (int slot : slotRange) {
            ItemStack stack = InteractionHandler.getStackFromSlot(slot);
            if (InteractionHandler.getCursorStack().isEmpty()) {
                handleEmptyCursorStack(slotRange, targetStack);
                break;
            } else if (!ItemStack.areItemsEqual(InteractionHandler.getCursorStack(), stack)) {
                handleFullCursorStack(slotRange, targetStack, slot);
                break;
            }
        }
        rearrangeSlots(slotRange);
    }

    private static void handleEmptyCursorStack(SlotRange slotRange, ItemStack targetStack) {
        List<Integer> sameStacks = findSameStacks(slotRange, targetStack);
        if (sameStacks.size() == 1) {
            InteractionHandler.leftClickStack(sameStacks.getFirst());
            while (InteractionHandler.getCursorStack().getCount() > targetStack.getCount()) {
                InteractionHandler.rightClickStack(sameStacks.getFirst());
            }
        } else {
            int lastSlot = sameStacks.getLast();
            InteractionHandler.leftClickStack(lastSlot);
            if (InteractionHandler.getCursorStack().getCount() > targetStack.getCount()) {
                while (InteractionHandler.getCursorStack().getCount() > targetStack.getCount()) {
                    InteractionHandler.rightClickStack(lastSlot);
                }
            } else if (InteractionHandler.getCursorStack().getCount() < targetStack.getCount()) {
                InteractionHandler.swapStacksTwoClicks(lastSlot, lastSlot - 1);
                while (targetStack.getCount() > InteractionHandler.getStackFromSlot(lastSlot).getCount()) {
                    InteractionHandler.rightClickStack(lastSlot);
                }
                InteractionHandler.swapStacksTwoClicks(lastSlot - 1, lastSlot);
            }
        }
    }

    private static void handleFullCursorStack(SlotRange slotRange, ItemStack targetStack, int currentSlot) {
        List<Integer> sameStacks = findSameStacks(slotRange, targetStack);
        InteractionHandler.leftClickStack(currentSlot);
        if (sameStacks.size() == 1) {
            InteractionHandler.leftClickStack(sameStacks.getFirst());
            while (targetStack.getCount() > InteractionHandler.getStackFromSlot(currentSlot).getCount()) {
                InteractionHandler.rightClickStack(currentSlot);
            }
            InteractionHandler.swapStacksTwoClicks(sameStacks.getFirst(), currentSlot);
        } else {
            int lastSlot = sameStacks.getLast();
            InteractionHandler.leftClickStack(lastSlot);
            while (targetStack.getCount() > InteractionHandler.getStackFromSlot(currentSlot).getCount()) {
                InteractionHandler.rightClickStack(currentSlot);
            }
            InteractionHandler.swapStacksTwoClicks(lastSlot, currentSlot);
        }
    }

    private static void rearrangeSlots(SlotRange slotRange) {
        List<Integer> slotsBefore = new ArrayList<>();
        for (int slot : slotRange) {
            if (InteractionHandler.getStackFromSlot(slot).isEmpty()) {
                SlotRange newSlotRange = slotRange.copy();
                for (int tempSlot : slotsBefore) {
                    newSlotRange.exclude(tempSlot);
                }
                for (int tempSlot : newSlotRange) {
                    if (tempSlot + 1 <= newSlotRange.getLast()) {
                        InteractionHandler.swapStacksThreeClicks(tempSlot + 1, tempSlot);
                    }
                }
            } else {
                slotsBefore.add(slot);
            }
        }
    }

    private static List<Integer> findSameStacks(SlotRange slotRange, ItemStack targetStack) {
        List<Integer> sameStacks = new ArrayList<>();
        for (int tempSlot : slotRange) {
            ItemStack tempStack = InteractionHandler.getStackFromSlot(tempSlot);
            if (ItemStack.areItemsEqual(tempStack, targetStack)) {
                sameStacks.add(tempSlot);
            }
        }
        return sameStacks;
    }
}
