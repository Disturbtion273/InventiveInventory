package net.origins.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.origins.inventive_inventory.util.Converter;
import net.origins.inventive_inventory.util.InteractionHandler;

import java.util.List;

public class Profile {
    private final int ID;
    private final String name;
    private final String key;
    private final ItemStack displayStack;
    private final List<SavedSlot> savedSlots;

    public Profile(int id, String name, String key, List<SavedSlot> savedSlots) {
        this.ID = id;
        this.name = name;
        this.key = key;
        this.savedSlots = savedSlots;
        this.displayStack = InteractionHandler.getAnyHandStack();
    }

    public Profile(int id, String name, String key, JsonObject displayStack, JsonArray savedSlots) {
        this.ID = id;
        this.name = name;
        this.key = key;
        this.displayStack = Converter.jsonToItemStack(displayStack);
        this.savedSlots = Converter.jsonToSavedSlots(savedSlots);
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
}
