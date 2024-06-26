package net.origins.inventive_inventory.features.automatic_refilling;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingBehaviours;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.util.Comparator;
import java.util.List;

public class AutomaticRefillingHandler {
    private static Item selectedItem;

    public static void setSelectedItem(ItemStack itemStack) {
        if (!itemStack.isEmpty()) {
            selectedItem = itemStack.getItem();
        }
    }

    public static void run() {
        // TODO: evtl. noch leere Eimer usw. wieder aufeinander stacken
        SlotRange slotRange = ConfigManager.AUTOMATIC_REFILLING_BEHAVIOUR == AutomaticRefillingBehaviours.IGNORE_LOCKED_SLOTS ? PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT) : PlayerSlots.get();
        List<Integer> sameItemSlotsHotbar = slotRange.copy().append(SlotTypes.HOTBAR).exclude(SlotTypes.INVENTORY).stream()
                .filter(slot -> selectedItem.equals(InteractionHandler.getStackFromSlot(slot).getItem()))
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()))
                .toList();
        List<Integer> sameItemSlotsInventory = slotRange.stream()
                .filter(slot -> selectedItem.equals(InteractionHandler.getStackFromSlot(slot).getItem()))
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()))
                .toList();

        if (sameItemSlotsHotbar.isEmpty() && !sameItemSlotsInventory.isEmpty()) {
            InteractionHandler.swapStacks(sameItemSlotsInventory.getFirst(), InteractionHandler.getSelectedSlot());
            selectedItem = null;
        } else if (!sameItemSlotsHotbar.isEmpty()) {
            InventiveInventory.getPlayer().getInventory().selectedSlot = sameItemSlotsHotbar.getFirst() - PlayerInventory.MAIN_SIZE;
            selectedItem = null;
        }
    }
}
