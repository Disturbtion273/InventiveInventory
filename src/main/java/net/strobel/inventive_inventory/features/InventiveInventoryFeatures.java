package net.strobel.inventive_inventory.features;

import net.minecraft.client.MinecraftClient;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.strobel.inventive_inventory.event.KeyInputHandler;

import java.util.*;


public class InventiveInventoryFeatures {
    public static void sortInventory(MinecraftClient client) {
        if (KeyInputHandler.sortInventoryKey.wasPressed()) {
            assert client.player != null;
            Inventory inventory = client.player.getInventory();

            Map<Item, List<ItemStack>> itemsByType = new HashMap<>();
            for (int i = 9; i < inventory.size(); i++) {
                ItemStack stack = inventory.getStack(i);
                if (!stack.isEmpty()) {
                    itemsByType.computeIfAbsent(stack.getItem(), k -> new ArrayList<>()).add(stack);
                }
            }

            List<ItemStack> combinedItems = new ArrayList<>();
            for (List<ItemStack> stacks : itemsByType.values()) {
                int totalItemCount = stacks.stream().mapToInt(ItemStack::getCount).sum();
                Item item = stacks.get(0).getItem();

                while (totalItemCount > 0) {
                    int stackSize = Math.min(totalItemCount, item.getMaxCount());
                    combinedItems.add(new ItemStack(item, stackSize));
                    totalItemCount -= stackSize;
                }
            }


            combinedItems.sort((stack1, stack2) -> {
                int nameComparison =
                        stack1.getItem().getName().getString().compareTo(stack2.getItem().getName().getString());
                if (nameComparison != 0) {
                    return nameComparison;
                } else {
                    return Integer.compare(stack2.getCount(), stack1.getCount());
                }
            });

            for (int i = 9; i < inventory.size(); i++) {
                inventory.setStack(i, ItemStack.EMPTY);
            }

            for (int i = 0; i < combinedItems.size(); i++) {
                inventory.setStack(i + 9, combinedItems.get(i));
            }
        }
    }
}
