package net.strobel.inventive_inventory.util;

import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;

import java.util.function.Predicate;

public class StreamUtils {
    public static Predicate<Integer> sameItem(Item item, ScreenHandler screenHandler) {
        return slot -> item.equals(screenHandler.getSlot(slot).getStack().getItem());
    }
}
