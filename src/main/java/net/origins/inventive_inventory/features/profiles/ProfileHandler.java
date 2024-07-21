package net.origins.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesLockedSlotsBehaviours;
import net.origins.inventive_inventory.util.ComponentsHelper;
import net.origins.inventive_inventory.util.FileHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ProfileHandler {
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = ConfigManager.CONFIG_PATH.resolve(PROFILES_FILE);

    public static void create(String name) {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<SavedSlot> savedSlots = new ArrayList<>();
        for (int slot : PlayerSlots.get(SlotTypes.HOTBAR, SlotTypes.OFFHAND)) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty()) {
                savedSlots.add(new SavedSlot(slot, stack));
            }
        }
        save(new Profile(getId(), name, savedSlots));
    }

    public static void load(Profile profile) {
        SlotRange slotRange = PlayerSlots.get(SlotTypes.INVENTORY, SlotTypes.HOTBAR, SlotTypes.OFFHAND);
        slotRange = ConfigManager.P_LS_BEHAVIOUR == ProfilesLockedSlotsBehaviours.IGNORE_LOCKED_SLOTS ? slotRange : slotRange.append(SlotTypes.LOCKED_SLOT);
        for (SavedSlot savedSlot : profile.getSavedSlots()) {
            for (int slot : slotRange) {
                ItemStack slotStack = InteractionHandler.getStackFromSlot(slot);
                if (!ItemStack.areItemsEqual(slotStack, savedSlot.stack())) continue;
                if (!ComponentsHelper.areCustomNamesEqual(slotStack, savedSlot.stack())) continue;
                if (!ComponentsHelper.areEnchantmentsEqual(slotStack, savedSlot.stack())) continue;
                if (!ComponentsHelper.arePotionsEqual(slotStack, savedSlot.stack())) continue;
                InteractionHandler.swapStacks(slot, savedSlot.slot());
                break;
            }
        }
    }

    public static void overwrite(Profile profile) {
        delete(profile);
        create("");
    }

    public static void delete(Profile profile) {
        JsonObject profilesJson = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        profilesJson.remove(Integer.toString(profile.getID()));
        FileHandler.write(PROFILES_PATH, profilesJson);
    }

    public static List<Profile> getProfiles() {
        JsonObject profilesJson = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        List<Profile> profiles = new ArrayList<>();
        for (String id : profilesJson.keySet()) {
            JsonObject jsonProfile = profilesJson.getAsJsonObject(id);
            profiles.add(new Profile(Integer.parseInt(id), jsonProfile.get("name").getAsString(), jsonProfile.get("key").getAsString(), jsonProfile.getAsJsonObject("display_stack"), jsonProfile.getAsJsonArray("saved_slots")));
        }
        return profiles;
    }

    private static void save(Profile profile) {
        JsonObject jsonProfile = new JsonObject();
        jsonProfile.addProperty("name", profile.getName());
        jsonProfile.addProperty("key", profile.getKey());
        jsonProfile.add("display_stack", profile.getDisplayStackAsJsonObject());

        JsonArray jsonArray = new JsonArray();
        for (SavedSlot savedSlot : profile.getSavedSlots()) {
            JsonObject savedSlotMap = new JsonObject();
            savedSlotMap.addProperty("slot", savedSlot.slot());
            savedSlotMap.add("stack", savedSlot.getItemStackAsJsonObject());
            jsonArray.add(savedSlotMap);
        }
        jsonProfile.add("saved_slots", jsonArray);

        JsonObject profiles = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        profiles.add(Integer.toString(profile.getID()), jsonProfile);
        FileHandler.write(ProfileHandler.PROFILES_PATH, profiles);
    }
    
    public static boolean profileExists(String name) {
        for (Profile profile : getProfiles()) {
            if (profile.getName().equals(name)) return true;
        } return false;
    }

    private static int getId() {
        JsonObject jsonObject = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        if (jsonObject.isEmpty()) return 0;
        return Integer.parseInt(jsonObject.keySet().stream().sorted(Comparator.comparing(Integer::valueOf)).toList().getLast()) + 1;
    }
    
}
