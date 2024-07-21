package net.origins.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public record SavedSlot(int slot, ItemStack stack) {

    public JsonObject getItemStackAsJsonObject() {
        JsonObject stackJson = new JsonObject();
        stackJson.addProperty("id", Item.getRawId(this.stack.getItem()));

        JsonObject componentsJson = new JsonObject();
        Text customName = this.stack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) componentsJson.addProperty("custom_name", customName.getString());

        ItemEnchantmentsComponent enchantmentsComponent = this.stack.get(DataComponentTypes.ENCHANTMENTS);
        if (enchantmentsComponent != null && !enchantmentsComponent.isEmpty()) {
            JsonArray enchantmentsList = new JsonArray();
            for (RegistryEntry<Enchantment> enchantmentRegistryEntry : enchantmentsComponent.getEnchantments().stream().toList()) {
                JsonObject enchantmentComponent = new JsonObject();
                enchantmentComponent.addProperty("id", enchantmentRegistryEntry.getIdAsString());
                enchantmentComponent.addProperty("lvl", enchantmentsComponent.getLevel(enchantmentRegistryEntry));
                enchantmentsList.add(enchantmentComponent);
            }
            componentsJson.add("enchantments", enchantmentsList);
        }

        PotionContentsComponent potionComponent = this.stack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionComponent != null) {
            if (potionComponent.potion().isPresent()) {
                componentsJson.addProperty("potion", potionComponent.potion().get().getIdAsString());
            }
        }

        stackJson.add("components", componentsJson);
        return stackJson;
    }
}
