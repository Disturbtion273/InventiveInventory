package net.strobel.inventive_inventory.features.sorting;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.HopperScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.strobel.inventive_inventory.InventiveInventoryClient;


public class Sorter {

    public static MinecraftClient client;
    public static ClientPlayerEntity player;
    public static ClientPlayerInteractionManager interactionManager;

    public static void sortPlayerInventory() {
        updateInstances();
        int syncId = player.currentScreenHandler.syncId;
        Inventory inventory = getPlayerInventory();
        int firstSlot = 9;
        int size = inventory.size() - 5;

        if (player.isCreative() && !player.playerScreenHandler.getCursorStack().isEmpty()) {
            for (int i = firstSlot; i < size; i++) {
                if (getPlayerInventory().getStack(i).isEmpty()) {
                    interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    break;
                }
            }
        }

        combineInventoryStacks(inventory, size, firstSlot, syncId);
        alignStacks(inventory, size, firstSlot, syncId);
        sort(inventory, size, firstSlot, syncId);
        sortByCount(inventory, size, firstSlot, syncId);
    }

    public static void sortContainerInventory() {
        updateInstances();
        int syncId = player.currentScreenHandler.syncId;
        Inventory inventory = getContainerInventory();
        int firstSlot = 0;
        int size = inventory.size();

        if (player.isCreative() && !player.playerScreenHandler.getCursorStack().isEmpty()) {
            for (int i = firstSlot; i < size; i++) {
                if (getPlayerInventory().getStack(i).isEmpty()) {
                    interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    break;
                }
            }
        }

        combineInventoryStacks(inventory, size, firstSlot, syncId);
        alignStacks(inventory, size, firstSlot, syncId);
        sort(inventory, size, firstSlot, syncId);
        sortByCount(inventory, size, firstSlot, syncId);
    }

    public static void sortHopperInventory() {
        updateInstances();
        int syncId = player.currentScreenHandler.syncId;
        Inventory inventory = getHopperInventory();
        int firstSlot = 0;
        int size = inventory.size();

        if (player.isCreative() && !player.playerScreenHandler.getCursorStack().isEmpty()) {
            for (int i = firstSlot; i < size; i++) {
                if (getPlayerInventory().getStack(i).isEmpty()) {
                    interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    break;
                }
            }
        }

        combineInventoryStacks(inventory, size, firstSlot, syncId);
        inventory = getHopperInventory();
        alignStacks(inventory, size, firstSlot, syncId);
        inventory = getHopperInventory();
        sort(inventory, size, firstSlot, syncId);
        inventory = getHopperInventory();
        sortByCount(inventory, size, firstSlot, syncId);
    }

    public static void sort3x3Container() {
        updateInstances();
        int syncId = player.currentScreenHandler.syncId;
        Inventory inventory = getContainer3x3Inventory();
        int firstSlot = 0;
        int size = inventory.size();

        if (player.isCreative() && !player.playerScreenHandler.getCursorStack().isEmpty()) {
            for (int i = firstSlot; i < size; i++) {
                if (getPlayerInventory().getStack(i).isEmpty()) {
                    interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    break;
                }
            }
        }

        combineInventoryStacks(inventory, size, firstSlot, syncId);
        inventory = getContainer3x3Inventory();
        alignStacks(inventory, size, firstSlot, syncId);
        inventory = getContainer3x3Inventory();
        sort(inventory, size, firstSlot, syncId);
        inventory = getContainer3x3Inventory();
        sortByCount(inventory, size, firstSlot, syncId);
    }


    private static void combineInventoryStacks(Inventory inventory, int size, int firstSlot, int syncId) {
        ScreenHandler handler = player.currentScreenHandler;

        if (!handler.getCursorStack().isEmpty()) {
            int i = firstSlot;
            boolean placedStack = false;
            while (i < size && !handler.getCursorStack().isEmpty()) {
                ItemStack stack = inventory.getStack(i);
                if (stack.isEmpty() || (ItemStack.areItemsEqual(handler.getCursorStack(), stack) && stack.getCount() < stack.getMaxCount())) {
                    interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    placedStack = true;
                }
                i++;
            }
            if (!placedStack) {
                return;
            }
        }

        for (int i = size - 1; i > firstSlot; i--) {
            if (player.currentScreenHandler instanceof Generic3x3ContainerScreenHandler) {
                inventory = getContainer3x3Inventory();
            } else if (player.currentScreenHandler instanceof HopperScreenHandler) {
                inventory = getHopperInventory();
            }
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty() && stack.getCount() != stack.getMaxCount()) {
                interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
            }

            for (int j = i - 1; j > firstSlot && !handler.getCursorStack().isEmpty(); j--) {
                ItemStack tempStack = inventory.getStack(j);
                if (tempStack.isEmpty() || tempStack.getCount() < tempStack.getMaxCount() && ItemStack.areItemsEqual(handler.getCursorStack(), tempStack)) {
                    interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, player);
                }
            }
            if (!handler.getCursorStack().isEmpty()) {
                interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
            }
        }

        if (!handler.getCursorStack().isEmpty()) {
            for (int i = firstSlot; i < size; i++) {
                ItemStack stack = inventory.getStack(i);
                if (stack.isEmpty()) {
                    interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    break;
                }
            }
        }
    }

    private static void alignStacks(Inventory inventory, int size, int firstSlot, int syncId) {
        for (int i = size - 1; i >= firstSlot; i--) {
            if (player.currentScreenHandler instanceof Generic3x3ContainerScreenHandler) {
                inventory = getContainer3x3Inventory();
            } else if (player.currentScreenHandler instanceof HopperScreenHandler) {
                inventory = getHopperInventory();
            }
            if (!inventory.getStack(i).isEmpty()) {
                for (int j = firstSlot; j < i; j++) {
                    if (inventory.getStack(j).isEmpty()) {
                        interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                        interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, player);
                        break;
                    }
                }
            }
        }
    }

    private static void sort(Inventory inventory, int size, int firstSlot, int syncId) {
        for (int i = firstSlot; i < size; i++) {
            if (player.currentScreenHandler instanceof Generic3x3ContainerScreenHandler) {
                inventory = getContainer3x3Inventory();
            } else if (player.currentScreenHandler instanceof HopperScreenHandler) {
                inventory = getHopperInventory();
            }
            for (int j = i + 1; j < size; j++) {
                if (!inventory.getStack(i).isEmpty() && !inventory.getStack(j).isEmpty()) {
                    int nameComparison = inventory.getStack(i).getName().getString().compareTo(inventory.getStack(j).getName().getString());
                    if (nameComparison > 0) {
                        interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                        interactionManager.clickSlot(syncId, j, 0, SlotActionType.PICKUP, player);
                        interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    }
                }
            }
        }
    }

    private static void sortByCount(Inventory inventory, int size, int firstSlot, int syncId) {
        for (int i = firstSlot; i < size; i++) {
            if (player.currentScreenHandler instanceof Generic3x3ContainerScreenHandler) {
                inventory = getContainer3x3Inventory();
            } else if (player.currentScreenHandler instanceof HopperScreenHandler) {
                inventory = getHopperInventory();
            }
            ItemStack stack = inventory.getStack(i);
            if (!(stack.getCount() == stack.getMaxCount())) {
                ItemStack tempStack = inventory.getStack(i + 1);
                if (ItemStack.areItemsEqual(stack, tempStack) && !stack.isEmpty()) {
                    interactionManager.clickSlot(syncId, i + 1, 0, SlotActionType.PICKUP, player);
                    interactionManager.clickSlot(syncId, i, 0, SlotActionType.PICKUP, player);
                    interactionManager.clickSlot(syncId, i + 1, 0, SlotActionType.PICKUP, player);
                }
            }
        }
    }

    private static Inventory getPlayerInventory() {
        return player.getInventory();
    }

    private static Inventory getContainerInventory() {
        GenericContainerScreenHandler handler = (GenericContainerScreenHandler) player.currentScreenHandler;
        return handler.getInventory();
    }

    private static Inventory getContainer3x3Inventory() {
        updateInstances();
        Generic3x3ContainerScreenHandler handler = (Generic3x3ContainerScreenHandler) player.currentScreenHandler;
        return new SimpleInventory(handler.getStacks().subList(0, 9).toArray(new ItemStack[0]));
    }

    private static Inventory getHopperInventory() {
        updateInstances();
        HopperScreenHandler handler = (HopperScreenHandler) player.currentScreenHandler;
        return new SimpleInventory(handler.getStacks().subList(0, 5).toArray(new ItemStack[0]));
    }


    public static void updateInstances() {
        client = InventiveInventoryClient.getClient();
        player = client.player;
        interactionManager = client.interactionManager;
    }
}
