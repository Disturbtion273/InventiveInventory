package net.strobel.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.ArrayList;
import java.util.List;

public class Profile {
    private final String name;
    private final List<SavedSlot> savedSlots = new ArrayList<>();

    public Profile(String name) {
        this.name = name;
        load();
        save();
    }

    private void load() {
        JsonObject profile = FileHandler.getJsonObject(ProfileHandler.PROFILES_PATH, this.name);
        JsonArray savedSlots = profile.get("saved_slots").getAsJsonArray();
        for (JsonElement savedSlotElement : savedSlots) {
            JsonObject savedSlotObject = savedSlotElement.getAsJsonObject();
            SavedSlot savedSlot = new SavedSlot(
                    savedSlotObject.get("slot").getAsInt(),
                    savedSlotObject.get("id").getAsString(),
                    savedSlotObject.get("nbt_data").getAsJsonObject());
            this.savedSlots.add(savedSlot);
        }
    }

    private void save() {
        JsonObject profiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH);
        if (profiles.has(this.name)) {
            profiles.remove(this.name);
        }

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
    }
}

