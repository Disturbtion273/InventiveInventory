package net.origins.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.util.InteractionHandler;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private final int ID;
    private final String name;
    private final String key;
    private final ItemStack displayStack;
    private final List<SavedSlot> savedSlots;

    public Profile(int id, String name, List<SavedSlot> savedSlots) {
        this(id, name, "", savedSlots);
    }

    public Profile(int id, String name, String key, List<SavedSlot> savedSlots) {
        this.ID = id;
        this.name = name;
        this.key = key;
        this.savedSlots = savedSlots;
        ItemStack stack = InteractionHandler.getMainHandStack();
        if (stack.isEmpty()) stack = InteractionHandler.getOffHandStack();
        if (stack.isEmpty()) stack = null;
        this.displayStack = stack;
    }

    public Profile(int id, String name, String key, JsonObject displayStack, JsonArray savedSlots) {
        this.ID = id;
        this.name = name;
        this.key = key;
        this.displayStack = convertJsonToItemStack(displayStack);
        this.savedSlots = convertJsonToSavedSlots(savedSlots);
    }

    public int getID() {
        return this.ID;
    }

    public String getName() {
        return this.name;
    }

    public String getKey() {
        return this.key;
    }

    public List<SavedSlot> getSavedSlots() {
        return this.savedSlots;
    }

    public ItemStack getDisplayStack() {
        return this.displayStack;
    }

    public JsonObject getDisplayStackAsJsonObject() {
        JsonObject displayStackJson = new JsonObject();
        if (this.displayStack == null) return displayStackJson;
        displayStackJson.addProperty("id", Item.getRawId(this.displayStack.getItem()));

        JsonObject components = new JsonObject();
        Text customName = this.displayStack.get(DataComponentTypes.CUSTOM_NAME);
        if (customName != null) {
            components.addProperty("custom_name", customName.getString());
        }

        ItemEnchantmentsComponent enchantmentsComponent = this.displayStack.get(DataComponentTypes.ENCHANTMENTS);
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

        PotionContentsComponent potionComponent = this.displayStack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionComponent != null) {
            if (potionComponent.potion().isPresent()) {
                components.addProperty("potion", potionComponent.potion().get().getIdAsString());
            }
        }

        displayStackJson.add("components", components);
        return displayStackJson;
    }

    public static ItemStack convertJsonToItemStack(JsonObject stackJson) {
        if (stackJson.get("id") == null) return null;
        ItemStack item = new ItemStack(RegistryEntry.of(Item.byRawId(stackJson.get("id").getAsInt())));
        ComponentMap.Builder componentBuilder = ComponentMap.builder();

        if (stackJson.getAsJsonObject("components").has("custom_name")) {
            componentBuilder.add(DataComponentTypes.CUSTOM_NAME, Text.of(stackJson.getAsJsonObject("components").get("custom_name").getAsString()));
        }

        if (stackJson.getAsJsonObject("components").has("enchantments")) {
            ItemEnchantmentsComponent.Builder enchantmentBuilder = new ItemEnchantmentsComponent.Builder(ItemEnchantmentsComponent.DEFAULT);
            Registry<Enchantment> enchantmentRegistry = InventiveInventory.getRegistryManager().get(RegistryKeys.ENCHANTMENT);
            for (JsonElement enchantmentElement : stackJson.getAsJsonObject("components").get("enchantments").getAsJsonArray()) {
                JsonObject enchantmentObject = enchantmentElement.getAsJsonObject();
                Enchantment enchantment = enchantmentRegistry.get(Identifier.of(enchantmentObject.get("id").getAsString()));
                enchantmentBuilder.add(enchantmentRegistry.getEntry(enchantment), enchantmentObject.get("lvl").getAsInt());
            }
            componentBuilder.add(DataComponentTypes.ENCHANTMENTS, enchantmentBuilder.build());
        }

        if (stackJson.getAsJsonObject("components").has("potion")) {
            Registry<Potion> potionRegistry = InventiveInventory.getRegistryManager().get(RegistryKeys.POTION);
            Potion potion = potionRegistry.get(Identifier.of(stackJson.getAsJsonObject("components").get("potion").getAsString()));
            item = PotionContentsComponent.createStack(Items.POTION, potionRegistry.getEntry(potion));
        }

        item.applyComponentsFrom(componentBuilder.build());
        return item;
    }

    private static List<SavedSlot> convertJsonToSavedSlots(JsonArray savedSlotsJson) {
        List<SavedSlot> savedSlotList = new ArrayList<>();
        for (JsonElement savedSlotElement : savedSlotsJson) {
            JsonObject savedSlotObject = savedSlotElement.getAsJsonObject();
            int slot = savedSlotObject.get("slot").getAsInt();
            ItemStack stack = convertJsonToItemStack(savedSlotObject.getAsJsonObject("stack"));
            savedSlotList.add(new SavedSlot(slot, stack));
        }
        return savedSlotList;
    }
}
