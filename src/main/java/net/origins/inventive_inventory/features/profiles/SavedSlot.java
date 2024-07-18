package net.origins.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public class SavedSlot {
    private final int slot;
    private final String itemID;
    private final ComponentMap components;

    public SavedSlot(int slot, String itemID, ComponentMap components) {
        this.slot = slot;
        this.itemID = itemID;
        this.components = components;
    }

    public int getSlot() {
        return slot;
    }

    public String getItemID() {
        return itemID;
    }

    public JsonObject getComponentsAsJsonObject() {
        JsonObject components = new JsonObject();

        Text customName = this.components.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) {
            components.addProperty("custom_name", customName.getString());
        }

        ItemEnchantmentsComponent enchantmentsComponent = this.components.get(DataComponentTypes.ENCHANTMENTS);
        if (enchantmentsComponent != null && !enchantmentsComponent.isEmpty()) {
            JsonArray enchantmentsList = new JsonArray();
            for (RegistryEntry<Enchantment> enchantmentRegistryEntry : enchantmentsComponent.getEnchantments().stream().toList()) {
                JsonObject enchantmentComponent = new JsonObject();
                enchantmentComponent.addProperty("id", enchantmentRegistryEntry.getIdAsString());
                enchantmentComponent.addProperty("lvl", enchantmentsComponent.getLevel(enchantmentRegistryEntry));
                enchantmentsList.add(enchantmentComponent);
            }
            components.add("enchantments", enchantmentsList);
        }

        return components;
    }
}
