package net.origins.inventive_inventory.util;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.item.ItemStack;

public class ComponentsHelper {

    public static boolean arePotionsEqual(ItemStack stack, ItemStack otherStack) {
        PotionContentsComponent potionContentsComponent = stack.get(DataComponentTypes.POTION_CONTENTS);
        PotionContentsComponent otherPotionContentsComponent = otherStack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent == null || otherPotionContentsComponent == null) return false;
        if (otherPotionContentsComponent.potion().isEmpty() || !areCustomNamesEqual(stack, otherStack)) return false;
        return potionContentsComponent.matches(otherPotionContentsComponent.potion().get());
    }

    public static boolean areCustomNamesEqual(ItemStack stack, ItemStack otherStack) {
        return stack.get(DataComponentTypes.CUSTOM_NAME) == otherStack.get(DataComponentTypes.CUSTOM_NAME);
    }
}
