package net.strobel.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.slots.InventorySlots;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

class Profile {
    private final String name;
    private final List<SavedSlot> savedSlots = new ArrayList<>();

    public Profile(String name) {
        this.name = name;
        this.load();
    }

    private void load() {
        JsonObject profile = FileHandler.getJsonObject(ProfileHandler.PROFILES_PATH, this.name);
        if (profile.isEmpty()) return;

        try {
            JsonArray savedSlots = profile.get("saved_slots").getAsJsonArray();
            for (JsonElement savedSlotElement : savedSlots) {
                JsonObject savedSlotObject = savedSlotElement.getAsJsonObject();
                SavedSlot savedSlot = new SavedSlot(
                        savedSlotObject.get("slot").getAsInt(),
                        savedSlotObject.get("id").getAsString(),
                        savedSlotObject.get("nbt_data").getAsJsonObject());
                this.savedSlots.add(savedSlot);
            }
        } catch (NullPointerException ignored) {}
    }

    public void create() {
        this.savedSlots.clear();
        InventorySlots inventorySlots = PlayerSlots.getHotbarAndEquipment();
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        for (int slot: inventorySlots) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty()) {
                String id = stack.getItem().toString();
                NbtCompound nbt = stack.getNbt();
                SavedSlot savedSlot = new SavedSlot(slot, id, nbt);
                this.savedSlots.add(savedSlot);
            }
        }
        this.save();
    }

    private void save() {
        JsonObject profiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH);

        JsonArray jsonArray = new JsonArray();
        for (SavedSlot savedSlot: this.savedSlots) {
            JsonObject savedSlotMap = new JsonObject();
            savedSlotMap.addProperty("slot", savedSlot.getSlot());
            savedSlotMap.addProperty("id", savedSlot.getId());
            savedSlotMap.add("nbt_data", savedSlot.convertNbtToJsonObject());
            jsonArray.add(savedSlotMap);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("saved_slots", jsonArray);

        profiles.add(this.name, jsonObject);
        FileHandler.write(ProfileHandler.PROFILES_PATH, profiles);
    }

    public List<SavedSlot> getSavedSlots() {
        return savedSlots;
    }
}

