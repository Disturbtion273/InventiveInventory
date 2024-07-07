package net.origins.inventive_inventory.features.automatic_refilling;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingBehaviours;
import net.origins.inventive_inventory.config.enums.automatic_refilling.AutomaticRefillingToolBehaviours;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutomaticRefillingHandler {
    public static boolean ATTACK_KEY_PRESSED = false;
    private static Item selectedItem = Items.AIR;
    private static final List<Class<? extends Item>> TOOL_CLASSES = List.of(SwordItem.class, PickaxeItem.class, AxeItem.class, ShovelItem.class, HoeItem.class, BowItem.class, CrossbowItem.class, TridentItem.class, MaceItem.class);

    public static void setSelectedItem(ItemStack itemStack) {
        selectedItem = itemStack.getItem();
    }

    public static void run() {
        boolean selectedItemAndMainHandItemIsStackable = selectedItem.getDefaultStack().isStackable() && InteractionHandler.getMainHandStack().isStackable();
        boolean mainHandItemIsFull_And_selectedItemAndMainHandItemIsStackable = !InteractionHandler.getMainHandStack().isEmpty() && selectedItemAndMainHandItemIsStackable;
        boolean mainHandItemIsNotBroken = InteractionHandler.getMainHandStack().getDamage() < InteractionHandler.getMainHandStack().getMaxDamage();
        boolean mainHandIsSelectedItem = InteractionHandler.getMainHandStack().getItem().equals(selectedItem);
        if (selectedItem.equals(Items.AIR) || mainHandItemIsFull_And_selectedItemAndMainHandItemIsStackable || mainHandItemIsNotBroken || mainHandIsSelectedItem) return;
        // TODO: evtl. noch leere Eimer usw. wieder aufeinander stacken
        // TODO: Wenn man Potions drinkt, wird nicht auf die nächste volle Potion geswitched. Vermutlich bei allen KonsumItems ein Problem, da es bei Essen auch nicht funktioniert
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
        SlotRange hotbarSlotRange = new SlotRange(sameItemSlots).exclude(SlotTypes.INVENTORY);
        SlotRange inventorySlotRange = new SlotRange(sameItemSlots).exclude(SlotTypes.HOTBAR);
        boolean hotBarSwap = !hotbarSlotRange.isEmpty();

        sameItemSlots.removeAll(hotBarSwap ? inventorySlotRange : hotbarSlotRange);
        if (sameItemSlots.isEmpty()) return;

        if (hotBarSwap) InteractionHandler.setSelectedSlot(sameItemSlots.getFirst() - PlayerInventory.MAIN_SIZE);
        else InteractionHandler.swapStacks(sameItemSlots.getFirst(), InteractionHandler.getSelectedSlot());

        selectedItem = Items.AIR;
    }
}
