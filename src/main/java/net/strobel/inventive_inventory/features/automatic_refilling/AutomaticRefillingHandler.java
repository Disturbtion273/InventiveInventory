package net.strobel.inventive_inventory.features.automatic_refilling;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.handler.InteractionHandler;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import java.util.Comparator;
import java.util.List;

public class AutomaticRefillingHandler {
    private static Item selectedItem;

    public static void setSelectedItem(ItemStack itemStack) {
        selectedItem = itemStack.getItem();
    }

    public static void run() {
        if (selectedItem == null || selectedItem.equals(Items.AIR) || !InventiveInventory.getPlayer().getMainHandStack().isEmpty()) return;

        List<Integer> slotsWithSameItemHotbar = PlayerSlots.getHotbar().stream()
                .filter(slot -> selectedItem.equals(InteractionHandler.getStackFromSlot(slot).getItem()))
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()))
                .toList();
        List<Integer> slotsWithSameItemInventory = PlayerSlots.getInventory().stream()
                .filter(slot -> selectedItem.equals(InteractionHandler.getStackFromSlot(slot).getItem()))
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()))
                .toList();

        if (slotsWithSameItemHotbar.isEmpty() && !slotsWithSameItemInventory.isEmpty()) {
            InteractionHandler.swapStacks(slotsWithSameItemInventory.getFirst(), InteractionHandler.getSelectedSlot());
        } else if (!slotsWithSameItemHotbar.isEmpty()) {
            InventiveInventory.getPlayer().getInventory().selectedSlot = slotsWithSameItemHotbar.getFirst() - PlayerInventory.MAIN_SIZE;
        }
        selectedItem = null;
    }
}
