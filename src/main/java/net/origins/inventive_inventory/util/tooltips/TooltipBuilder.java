package net.origins.inventive_inventory.util.tooltips;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.keys.KeyRegistry;


import java.util.ArrayList;
import java.util.List;

public class TooltipBuilder {

    public static List<Text> of(TooltipType type, Profile profile) {
        if (type == TooltipType.NAME) return buildName(profile);
        else if (type == TooltipType.ITEM) return buildItem(profile);
        else if (type == TooltipType.UNKNOWN) return buildUnknown(profile);
        else return new ArrayList<>();
    }

    private static List<Text> buildName(Profile profile) {
        List<Text> textList = new ArrayList<>();
        addTitle(profile.getName(), Formatting.GOLD, textList);
        addKey(profile, textList);
        return textList;
    }

    private static List<Text> buildItem(Profile profile) {
        List<Text> textList = new ArrayList<>();
        addTitle(profile.getDisplayStack().getName().getString(), Formatting.AQUA, textList);
        if (profile.getDisplayStack().hasEnchantments()) {
            for (RegistryEntry<Enchantment> entry : profile.getDisplayStack().getEnchantments().getEnchantments()) {
                textList.add(Enchantment.getName(entry, EnchantmentHelper.getLevel(entry, profile.getDisplayStack())));
            }
            textList.add(Text.empty());
        }
        addKey(profile, textList);
        return textList;
    }

    private static List<Text> buildUnknown(Profile profile) {
        List<Text> textList = new ArrayList<>();
        addTitle("Unnamed", Formatting.GRAY, textList);
        addKey(profile, textList);
        return textList;
    }

    private static void addTitle(String title, Formatting formatting, List<Text> textList) {
        textList.add(Text.of(title).copy().setStyle(Style.EMPTY.withColor(formatting)));
    }

    private static void addKey(Profile profile, List<Text> textList) {
        if (profile.getKey() != null) {
            KeyBinding keyBinding = KeyRegistry.getByTranslationKey(profile.getKey());
            if (keyBinding != null) textList.add(Text.of("Key: " + keyBinding.getBoundKeyLocalizedText().getString()));
        }
    }
}
