package net.origins.inventive_inventory.features.automatic_refilling;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.screen.PlayerScreenHandler;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingLockedSlotsBehaviours;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingStatus;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingToolBehaviours;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.util.*;
import java.util.stream.Stream;

public class AutomaticRefillingHandler {

    public static boolean RUN_OFFHAND = true;
    private static ItemStack offHandStack = ItemStack.EMPTY;
    private static ItemStack mainHandStack = ItemStack.EMPTY;

    private static final List<Item> EMPTIES = List.of(Items.BUCKET, Items.GLASS_BOTTLE, Items.BOWL);
    private static final List<Class<? extends Item>> TOOL_CLASSES = List.of(SwordItem.class, PickaxeItem.class, AxeItem.class, ShovelItem.class, HoeItem.class, BowItem.class, CrossbowItem.class, TridentItem.class, MaceItem.class);

    public static void setOffHandStack(ItemStack itemStack) {
        offHandStack = itemStack.copy();
    }

    public static void setMainHandStack(ItemStack itemStack) {
        mainHandStack = itemStack.copy();
    }

    public static void runMainHand() {
        if (ConfigManager.AUTOMATIC_REFILLING == AutomaticRefillingStatus.DISABLED) return;
        if (ItemStack.areItemsEqual(mainHandStack, ItemStack.EMPTY) || ItemStack.areItemsEqual(mainHandStack, InteractionHandler.getMainHandStack())) return;

        List<Integer> sameItemSlots = getSameItemSlots(mainHandStack);

        int emptiesSlot = InteractionHandler.getSelectedSlot();
        if (!sameItemSlots.isEmpty()) {
            if (SlotRange.slotIn(SlotTypes.HOTBAR, sameItemSlots.getFirst())) InteractionHandler.setSelectedSlot(sameItemSlots.getFirst() - PlayerInventory.MAIN_SIZE);
            else {
                InteractionHandler.swapStacks(sameItemSlots.getFirst(), InteractionHandler.getSelectedSlot());
                emptiesSlot = sameItemSlots.getFirst();
            }
            RUN_OFFHAND = false;
            offHandStack = ItemStack.EMPTY;
        }

        mainHandStack = ItemStack.EMPTY;
        if (EMPTIES.contains(InteractionHandler.getStackFromSlot(emptiesSlot).getItem())) mergeEmpties(emptiesSlot);
    }

    public static void runOffHand() {
        if (ConfigManager.AUTOMATIC_REFILLING == AutomaticRefillingStatus.DISABLED) return;
        if (ItemStack.areItemsEqual(offHandStack, ItemStack.EMPTY) || ItemStack.areItemsEqual(offHandStack, InteractionHandler.getOffHandStack())) return;

        List<Integer> sameItemSlots = getSameItemSlots(offHandStack);

        int emptiesSlot = PlayerScreenHandler.OFFHAND_ID;
        if (!sameItemSlots.isEmpty()) {
            InteractionHandler.swapStacks(sameItemSlots.getFirst(), PlayerScreenHandler.OFFHAND_ID);
            emptiesSlot = sameItemSlots.getFirst();
        }

        offHandStack = ItemStack.EMPTY;
        if (EMPTIES.contains(InteractionHandler.getStackFromSlot(emptiesSlot).getItem())) mergeEmpties(emptiesSlot);
    }

    private static List<Integer> getSameItemSlots(ItemStack handStack) {
        SlotRange slotRange = PlayerSlots.get(SlotTypes.INVENTORY, SlotTypes.HOTBAR).exclude(InteractionHandler.getSelectedSlot());
        slotRange = ConfigManager.AR_LS_BEHAVIOUR == AutomaticRefillingLockedSlotsBehaviours.IGNORE ? slotRange.exclude(SlotTypes.LOCKED_SLOT) : slotRange;
        Stream<Integer> sameItemSlotsStream =  slotRange.stream()
                .filter(slot -> {
                    ItemStack itemStack = InteractionHandler.getStackFromSlot(slot);
                    boolean isEqual = ItemStack.areItemsAndComponentsEqual(handStack, itemStack);
                    boolean isToolAndEqual = handStack.getItem().getClass().equals(itemStack.getItem().getClass()) && TOOL_CLASSES.contains(handStack.getItem().getClass());
                    return isEqual || isToolAndEqual;
                });

        if (TOOL_CLASSES.contains(handStack.getItem().getClass())) {
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

        List<Integer> sameItemSlots = new ArrayList<>(sameItemSlotsStream.toList());
        SlotRange hotbarSlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.INVENTORY);
        SlotRange inventorySlotRange = SlotRange.of(sameItemSlots).exclude(SlotTypes.HOTBAR);
        sameItemSlots.removeAll(!hotbarSlotRange.isEmpty() ? inventorySlotRange : hotbarSlotRange);
        return sameItemSlots;
    }

    private static void mergeEmpties(int itemSlot) {
        SlotRange slotRange = PlayerSlots.get().append(SlotTypes.HOTBAR).exclude(itemSlot);
        slotRange = ConfigManager.AR_LS_BEHAVIOUR == AutomaticRefillingLockedSlotsBehaviours.IGNORE ? slotRange.exclude(SlotTypes.LOCKED_SLOT) : slotRange;
        List<Integer> sameItemSlots = slotRange.stream()
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getItem().equals(InteractionHandler.getStackFromSlot(itemSlot).getItem()))
                .filter(slot -> InteractionHandler.getStackFromSlot(slot).getCount() < InteractionHandler.getStackFromSlot(slot).getMaxCount())
                .sorted(Comparator.comparing(slot -> InteractionHandler.getStackFromSlot(slot).getCount(), Comparator.reverseOrder()))
                .toList();
        if (!sameItemSlots.isEmpty()) {
            InteractionHandler.swapStacksTwoClicks(itemSlot, sameItemSlots.getFirst());
        }
    }

    public static void reset() {
        AutomaticRefillingHandler.setMainHandStack(ItemStack.EMPTY);
        AutomaticRefillingHandler.setOffHandStack(ItemStack.EMPTY);
        AutomaticRefillingHandler.RUN_OFFHAND = true;
    }
}
