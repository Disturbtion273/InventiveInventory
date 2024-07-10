package net.origins.inventive_inventory.features.automatic_refilling;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingBehaviours;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingStatus;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingToolBehaviours;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutomaticRefillingHandler {
    public static boolean USE_KEY_PRESSED = false;
    public static boolean ATTACK_KEY_PRESSED = false;
    public static boolean IS_USING_ITEM = false;
    private static Item selectedItem = Items.AIR;
    private static final List<Item> EMPTIES = List.of(Items.BUCKET, Items.GLASS_BOTTLE, Items.BOWL);
    private static final List<Class<? extends Item>> TOOL_CLASSES = List.of(SwordItem.class, PickaxeItem.class, AxeItem.class, ShovelItem.class, HoeItem.class, BowItem.class, CrossbowItem.class, TridentItem.class, MaceItem.class);

    public static void setSelectedItem(ItemStack itemStack) {
        selectedItem = itemStack.getItem();
    }

    public static void run() {
        if (ConfigManager.AUTOMATIC_REFILLING == AutomaticRefillingStatus.DISABLED) return;
        if (selectedItem.equals(Items.AIR) || selectedItem.equals(InteractionHandler.getMainHandStack().getItem())) return;
        SlotRange slotRange = ConfigManager.AR_LS_BEHAVIOUR == AutomaticRefillingBehaviours.IGNORE_LOCKED_SLOTS ? PlayerSlots.get().exclude(SlotTypes.LOCKED_SLOT) : PlayerSlots.get();
        Stream<Integer> sameItemSlotsStream = slotRange.append(SlotTypes.HOTBAR).exclude(InteractionHandler.getSelectedSlot()).stream()
                .filter(slot -> {
                    Item item = InteractionHandler.getStackFromSlot(slot).getItem();
                    return selectedItem.equals(item) || (selectedItem.getClass().equals(item.getClass()) && TOOL_CLASSES.contains(selectedItem.getClass()));
                });

        if (TOOL_CLASSES.contains(selectedItem.getClass())) {
            if (ConfigManager.AR_TOOL_BEHAVIOUR == AutomaticRefillingToolBehaviours.MATERIAL) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage(), Comparator.reverseOrder()));
            } else if (ConfigManager.AR_TOOL_BEHAVIOUR == AutomaticRefillingToolBehaviours.HEALTH) {
                sameItemSlotsStream = sameItemSlotsStream
                        .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getMaxDamage() - InteractionHandler.getStackFromSlot(slot).getDamage()));
            }
        } else {
            sameItemSlotsStream = sameItemSlotsStream
                    .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount()));
        }

        List<Integer> sameItemSlots = sameItemSlotsStream.collect(Collectors.toList());
        SlotRange hotbarSlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.INVENTORY);
        SlotRange inventorySlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.HOTBAR);
        boolean hotBarSwap = !hotbarSlotRange.isEmpty();
        sameItemSlots.removeAll(hotBarSwap ? inventorySlotRange : hotbarSlotRange);

        int emptiesSlot = InteractionHandler.getSelectedSlot();
        if (!sameItemSlots.isEmpty()) {
            if (hotBarSwap) InteractionHandler.setSelectedSlot(sameItemSlots.getFirst() - PlayerInventory.MAIN_SIZE);
            else {
                InteractionHandler.swapStacks(sameItemSlots.getFirst(), InteractionHandler.getSelectedSlot());
                emptiesSlot = sameItemSlots.getFirst();
            }
            selectedItem = Items.AIR;
        }

        if (EMPTIES.contains(InteractionHandler.getStackFromSlot(emptiesSlot).getItem())) mergeEmpties(emptiesSlot);
    }

    private static void mergeEmpties(int itemSlot) {
        SlotRange slotRange = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(itemSlot);
        slotRange = ConfigManager.AR_LS_BEHAVIOUR == AutomaticRefillingBehaviours.IGNORE_LOCKED_SLOTS ? slotRange.exclude(SlotTypes.LOCKED_SLOT) : slotRange;
        List<Integer> sameItemSlots = slotRange.stream()
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getItem().equals(InteractionHandler.getStackFromSlot(itemSlot).getItem()))
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getCount() < InteractionHandler.getStackFromSlot(slot).getMaxCount())
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                .toList();
        if (!sameItemSlots.isEmpty()) {
            InteractionHandler.swapStacksTwoClicks(itemSlot, sameItemSlots.getFirst());
        }
    }
}
