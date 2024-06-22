package net.origins.inventive_inventory.features.sorting;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.sorting.SortingBehaviours;
import net.origins.inventive_inventory.config.sorting.SortingModes;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.SlotRange;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Sorter {
    public static void sortItemStacks(SlotRange slotRange, ScreenHandler screenHandler, ItemStack targetStack) {
        List<Integer> sortedSlots = new ArrayList<>(getSortedSlots(slotRange, screenHandler));
        try {
            for (int i = 0; i < sortedSlots.size(); i++) {
                if (sortedSlots.get(i).equals(slotRange.get(i))) continue;
                InteractionHandler.swapStacks(sortedSlots.get(i), slotRange.get(i));
                sortedSlots = getSortedSlots(slotRange, screenHandler);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (ConfigManager.SORTING_BEHAVIOUR == SortingBehaviours.KEEP_CURSOR_STACK) {
            Merger.adjustCursorStack(slotRange, screenHandler, targetStack);
        }
    }

    private static List<Integer> getSortedSlots(SlotRange slotRange, ScreenHandler screenHandler) {
        if (ConfigManager.SORTING_MODE == SortingModes.NAME) {
            return slotRange.stream()
                    .filter(slot -> !screenHandler.getSlot(slot).getStack().isEmpty())
                    .sorted(Comparator.comparing((Integer slot) -> screenHandler.getSlot(slot).getStack().getName().getString())
                            .thenComparing(slot -> screenHandler.getSlot(slot).getStack().getCount(), Comparator.reverseOrder()))
                    .toList();
        } else {
            return slotRange.stream()
                    .filter(slot -> !screenHandler.getSlot(slot).getStack().isEmpty())
                    .sorted(Comparator.comparing((Integer slot) -> Item.getRawId(screenHandler.getSlot(slot).getStack().getItem()))
                    )//.thenComparing(slot -> screenHandler.getSlot(slot).getStack().getCount(), Comparator.reverseOrder()))
                    .toList();
        }
    }
}
