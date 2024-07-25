package net.origins.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesLockedSlotsBehaviours;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesStatus;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.util.ComponentsHelper;
import net.origins.inventive_inventory.util.FileHandler;
import net.origins.inventive_inventory.util.InteractionHandler;
import net.origins.inventive_inventory.util.Notifier;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotRange;
import net.origins.inventive_inventory.util.slots.SlotTypes;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileHandler {
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = ConfigManager.CONFIG_PATH.resolve(PROFILES_FILE);
    public static final int MAX_PROFILES = 5;

    public static void create(String name, String key) {
        if (InventiveInventory.getPlayer().isInCreativeMode() || ConfigManager.PROFILES == ProfilesStatus.DISABLED) return;
        JsonArray profilesJson = getJsonProfiles();
        Profile profile = new Profile(profilesJson.size(), name, key, createSavedSlots());
        if (profilesJson.size() < MAX_PROFILES) {
            profilesJson.add(profile.getAsJsonObject());
            save(profilesJson);
            Notifier.send("Profile created!", Formatting.GREEN);
            return;
        }
        Notifier.error("Max amount of profiles reached!");
    }

    public static void load(Profile profile) {
        if (InventiveInventory.getPlayer().isInCreativeMode() || ConfigManager.PROFILES == ProfilesStatus.DISABLED) return;
        SlotRange slotRange = PlayerSlots.get(SlotTypes.INVENTORY, SlotTypes.HOTBAR, SlotTypes.OFFHAND);
        slotRange = ConfigManager.P_LS_BEHAVIOUR == ProfilesLockedSlotsBehaviours.IGNORE ? slotRange : slotRange.append(SlotTypes.LOCKED_SLOT);
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
        Notifier.send("Profile loaded!", Formatting.BLUE);
    }

    public static void overwrite(Profile profile) {
        if (InventiveInventory.getPlayer().isInCreativeMode() || ConfigManager.PROFILES == ProfilesStatus.DISABLED) return;
        JsonArray profilesJson = getJsonProfiles();
        Profile newProfile = new Profile(profile.getId(), profile.getName(), profile.getKey(), createSavedSlots());
        if (profilesJson.isEmpty()) profilesJson.add(newProfile.getAsJsonObject());
        else profilesJson.set(profile.getId(), newProfile.getAsJsonObject());

        save(profilesJson);
        Notifier.send("Profile overwritten!", Formatting.GOLD);
    }

    public static void update(Profile profile) {
        if (InventiveInventory.getPlayer().isInCreativeMode() || ConfigManager.PROFILES == ProfilesStatus.DISABLED) return;
        JsonArray profilesJson = getJsonProfiles();
        Profile newProfile = new Profile(profile.getId(), profile.getName(), profile.getKey(), profile.getSavedSlots());
        if (profilesJson.isEmpty()) profilesJson.add(newProfile.getAsJsonObject());
        else profilesJson.set(profile.getId(), newProfile.getAsJsonObject());

        save(profilesJson);
        Notifier.send("Profile updated!", Formatting.GOLD);
    }

    public static void delete(Profile profile) {
        if (InventiveInventory.getPlayer().isInCreativeMode() || ConfigManager.PROFILES == ProfilesStatus.DISABLED) return;
        JsonArray profilesJson = getJsonProfiles();
        if (!profilesJson.isEmpty()) profilesJson.remove(profile.getId());
        save(profilesJson);
        Notifier.send("Profile deleted!", Formatting.RED);
    }

    public static List<Profile> getProfiles() {
        List<Profile> profiles = new ArrayList<>();
        for (JsonElement profileElement : getJsonProfiles()) {
            JsonObject jsonProfile = profileElement.getAsJsonObject();
            if (profiles.size() < MAX_PROFILES) {
                profiles.add(new Profile(jsonProfile.get("id").getAsInt(), jsonProfile.get("name").getAsString(), jsonProfile.get("key").getAsString(), jsonProfile.getAsJsonObject("display_stack"), jsonProfile.getAsJsonArray("saved_slots")));
            }
        }
        return profiles;
    }
    
    public static boolean isNoProfile(String name) {
        for (Profile profile : getProfiles()) {
            if (profile.getName().equals(name)) return false;
        } return true;
    }

    public static String getAvailableProfileKey() {
        List<KeyBinding> availableProfileKeys = getAvailableProfileKeys();
        if (availableProfileKeys.isEmpty()) return "";
        else return availableProfileKeys.getFirst().getTranslationKey();
    }

    public static List<KeyBinding> getAvailableProfileKeys() {
        List<KeyBinding> availableProfileKeys = new ArrayList<>(Arrays.asList(KeyRegistry.profileKeys));
        for (Profile profile : getProfiles()) {
            for (KeyBinding profileKey : KeyRegistry.profileKeys) {
                if (profileKey.getTranslationKey().equals(profile.getKey())) availableProfileKeys.remove(profileKey);
            }
        }
        return availableProfileKeys;
    }

    private static void save(JsonArray profiles) {
        JsonObject jsonObject = FileHandler.get(PROFILES_PATH).isJsonObject() ? FileHandler.get(PROFILES_PATH).getAsJsonObject() : new JsonObject();
        jsonObject.remove(InventiveInventory.getWorldName());
        jsonObject.add(InventiveInventory.getWorldName(), profiles);
        FileHandler.write(ProfileHandler.PROFILES_PATH, jsonObject);
    }

    private static List<SavedSlot> createSavedSlots() {
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();
        List<SavedSlot> savedSlots = new ArrayList<>();
        for (int slot : PlayerSlots.get(SlotTypes.HOTBAR, SlotTypes.OFFHAND)) {
            ItemStack stack = screenHandler.getSlot(slot).getStack();
            if (!stack.isEmpty()) savedSlots.add(new SavedSlot(slot, stack));
        }
        return savedSlots;
    }

    private static JsonArray getJsonProfiles() {
        return FileHandler.get(PROFILES_PATH).isJsonObject() && FileHandler.get(PROFILES_PATH).getAsJsonObject().has(InventiveInventory.getWorldName()) ? FileHandler.get(PROFILES_PATH).getAsJsonObject().getAsJsonArray(InventiveInventory.getWorldName()) : new JsonArray();
    }
}
