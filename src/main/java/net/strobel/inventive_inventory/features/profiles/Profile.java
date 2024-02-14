package net.strobel.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

class Profile {
    private final String name;
    private final List<SavedSlot> savedSlots;

    private Profile(String name, List<SavedSlot> savedSlots) {
        this.name = name;
        this.savedSlots = savedSlots;
    }

    public static void create(String name) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<SavedSlot> savedSlots = new ArrayList<>();
        for (int slot : PlayerSlots.getHotbarAndEquipment()) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty()) {
                String id = stack.getItem().toString();
                NbtCompound nbt = stack.getNbt();
                savedSlots.add(new SavedSlot(slot, id, nbt));
            }
        }
        new Profile(name, savedSlots).save();
    }

    public static Profile load(String name) {
        JsonObject jsonProfile = FileHandler.getJsonObject(ProfileHandler.PROFILES_PATH, name);
        JsonArray jsonSavedSlots = jsonProfile.get("saved_slots").getAsJsonArray();

        List<SavedSlot> savedSlots = new ArrayList<>();
        for (JsonElement savedSlotElement : jsonSavedSlots) {
            JsonObject savedSlotObject = savedSlotElement.getAsJsonObject();
            SavedSlot savedSlot = new SavedSlot(
                    savedSlotObject.get("slot").getAsInt(),
                    savedSlotObject.get("id").getAsString(),
                    savedSlotObject.get("nbt_data").getAsJsonObject());
            savedSlots.add(savedSlot);
        }
        return new Profile(name, savedSlots);
    }

    private void save() {
        JsonArray jsonArray = new JsonArray();
        for (SavedSlot savedSlot : this.savedSlots) {
            JsonObject savedSlotMap = new JsonObject();
            savedSlotMap.addProperty("slot", savedSlot.getSlot());
            savedSlotMap.addProperty("id", savedSlot.getId());
            savedSlotMap.add("nbt_data", savedSlot.convertNbtToJsonObject());
            jsonArray.add(savedSlotMap);
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("saved_slots", jsonArray);

        JsonObject profiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH);
        profiles.add(this.name, jsonObject);
        FileHandler.write(ProfileHandler.PROFILES_PATH, profiles);
    }

    public List<SavedSlot> getSavedSlots() {
        return savedSlots;
    }
}

