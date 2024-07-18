package net.origins.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.component.ComponentChanges;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.util.FileHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ProfileHandler {
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = ConfigManager.CONFIG_PATH.resolve(PROFILES_FILE);

    public static void create(String name) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<SavedSlot> savedSlots = new ArrayList<>();
        for (int slot : PlayerSlots.get(SlotTypes.HOTBAR).append(SlotTypes.OFFHAND)) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty()) {
                String itemID = stack.getItem().toString();
                ComponentMap components = stack.getComponents()
                        .filtered(componentType -> componentType == DataComponentTypes.CUSTOM_NAME || componentType == DataComponentTypes.ENCHANTMENTS);
                savedSlots.add(new SavedSlot(slot, itemID, components));
            }
        }
        save(new Profile(getId(), name, savedSlots));
    }

    public static void load() {

    }

    public static void overwrite() {

    }

    public static void delete() {

    }

    public static List<Profile> getProfiles() {
        JsonObject profilesJson = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        List<Profile> profiles = new ArrayList<>();
        for (String id : profilesJson.keySet()) {
            JsonObject jsonProfile = profilesJson.getAsJsonObject(id);

            profiles.add(new Profile(Integer.parseInt(id), jsonProfile.get("name").getAsString(), jsonProfile.get("key").getAsString(), jsonProfile.getAsJsonObject("display_stack"), jsonProfile.getAsJsonArray("saved:slots")));
        }
    }

    private static void save(Profile profile) {
        JsonObject jsonProfile = new JsonObject();
        jsonProfile.addProperty("name", profile.getName());
        jsonProfile.addProperty("key", profile.getKey());
        jsonProfile.add("display_stack", profile.getDisplayStackAsJsonObject());

        JsonArray jsonArray = new JsonArray();
        for (SavedSlot savedSlot : profile.getSavedSlots()) {
            JsonObject savedSlotMap = new JsonObject();
            savedSlotMap.addProperty("slot", savedSlot.getSlot());
            savedSlotMap.addProperty("id", savedSlot.getItemID());
            savedSlotMap.add("components", savedSlot.getComponentsAsJsonObject());
            jsonArray.add(savedSlotMap);
        }
        jsonProfile.add("saved_slots", jsonArray);

        JsonObject profiles = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();

        profiles.add(Integer.toString(profile.getID()), jsonProfile);
        FileHandler.write(ProfileHandler.PROFILES_PATH, profiles);
    }
    
    public static boolean profileExists(String name) {
        return true;
    }

    private static int getId() {
        JsonObject jsonObject = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        if (jsonObject.isEmpty()) return 0;
        return Integer.parseInt(jsonObject.keySet().stream().sorted(Comparator.comparing(Integer::valueOf)).toList().getFirst()) + 1;
    }
    
}
