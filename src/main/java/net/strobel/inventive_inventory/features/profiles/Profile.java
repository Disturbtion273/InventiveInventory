package net.strobel.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandler;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.features.sorting.Sorter;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.keybindfix.MixinIKeyBindingDisplay;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.*;

class Profile {
    private final String name;
    private final String key;
    private final List<SavedSlot> savedSlots;

    private Profile(String name, String key, List<SavedSlot> savedSlots) {
        this.name = name;
        this.key = key;
        this.savedSlots = savedSlots;
        int i = 0;
        for (KeyBinding keyBinding : KeyInputHandler.profileKeys) {
            if (keyBinding.getBoundKeyLocalizedText().getString().equals(key)) {
                ((MixinIKeyBindingDisplay) keyBinding).main$setDisplayName(name);
                ProfileHandler.profileNames.set(i, name);
                break;
            }
            i++;
        }

    }

    public static void create(String name, String key) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<SavedSlot> savedSlots = new ArrayList<>();
        if (screenHandler == null) return; // TODO

        Sorter.mergeItemStacks(PlayerSlots.getWithHotbar().excludeLockedSlots(), screenHandler);

        for (int slot : PlayerSlots.getHotbarAndEquipment()) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty()) {
                String id = stack.getItem().toString();
                NbtCompound nbt = stack.getNbt();
                savedSlots.add(new SavedSlot(slot, id, nbt));
            }
        }
        new Profile(name, key, savedSlots).save();
    }

    public static Profile load(String name) {
        JsonObject jsonProfile = FileHandler.getJsonObject(ProfileHandler.PROFILES_PATH, name);
        String key = jsonProfile.get("keybind").getAsString();
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
        return new Profile(name, key, savedSlots);
    }

    public static void overwrite(String name, String key) {
        JsonObject allProfiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH);
        for (String profileName : allProfiles.keySet()) {
            String keybind = allProfiles.getAsJsonObject(profileName).get("keybind").getAsString();
            if (profileName.equals(name) || keybind.equals(key)) {
                ProfileHandler.delete(profileName, false);
            }
        }
        Profile.create(name, key);
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
        jsonObject.addProperty("keybind", this.key);
        jsonObject.add("saved_slots", jsonArray);

        JsonObject profiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH);
        profiles.add(this.name, jsonObject);
        FileHandler.write(ProfileHandler.PROFILES_PATH, profiles);
    }

    public List<SavedSlot> getSavedSlots() {
        return savedSlots;
    }
}

