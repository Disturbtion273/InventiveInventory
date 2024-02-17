package net.strobel.inventive_inventory.features.profiles;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.config.ConfigManager;
import net.strobel.inventive_inventory.features.sorting.Sorter;
import net.strobel.inventive_inventory.handler.InteractionHandler;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileHandler {
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = ConfigManager.PATH.resolve(PROFILES_FILE);

    // Commands
    public static void save(String name, String key) {
        if (key.isBlank()) {
            Profile.create(name, key);
        } else {
            JsonObject allProfiles = FileHandler.getJsonFile(PROFILES_PATH);
            for (String profileKey : allProfiles.keySet()) {
                JsonElement keybind = FileHandler.getJsonObject(ProfileHandler.PROFILES_PATH, profileKey).get("keybind");
                if (keybind.getAsString().equals(key) && !key.isBlank()) {
                    ProfileHandler.delete(profileKey);
                    Profile.create(name, key);
                    break;
                }
            }
        }

        Text text = Text.of("Saved: " + name).copy()
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true));
        InventiveInventory.getPlayer().sendMessage(text, true);
    }

    // Hotkeys
    public static  void save(String name, KeyBinding keyBinding) {
        Profile.create(name, keyBinding);
        Text text = Text.of("Saved: " + name).copy()
                .setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true));
        InventiveInventory.getPlayer().sendMessage(text, true);
    }

    public static void load(String name) {
        Profile profile = Profile.load(name);
        List<SavedSlot> savedSlots = profile.getSavedSlots();
        ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

        Sorter.mergeItemStacks(PlayerSlots.get().excludeLockedSlots(), screenHandler);

        for (SavedSlot savedSlot: savedSlots) {
            boolean matchFound = false;

            for (int i : PlayerSlots.getWithHotbarAndArmor()) {
                ItemStack stack = screenHandler.getSlot(i).getStack();
                NbtCompound stackNbt = stack.getNbt();
                if (stack.getItem().toString().equals(savedSlot.getId()) && stackNbt != null && savedSlot.getNbtData() != null) {
                    if (stackNbt.getCompound("display").getString("Name").equals(savedSlot.getNbtData().getString("custom_name"))) {
                        if (equalNbt(stackNbt, savedSlot.getNbtData())) {
                            InteractionHandler.swapStacks(savedSlot.getSlot(), i);
                            matchFound = true;
                            break;
                        }
                    }
                }
            }
            if (matchFound) continue;

            for (int i : PlayerSlots.getWithHotbarAndArmor()) {
                ItemStack stack = screenHandler.getSlot(i).getStack();
                NbtCompound stackNbt = stack.getNbt();
                if (stack.getItem().toString().equals(savedSlot.getId()) && stackNbt != null && savedSlot.getNbtData() != null) {
                    if (equalNbt(stackNbt, savedSlot.getNbtData())) {
                        InteractionHandler.swapStacks(savedSlot.getSlot(), i);
                        matchFound = true;
                        break;
                    }
                }
            }
            if (matchFound) continue;

            for (int i : PlayerSlots.getWithHotbarAndArmor()) {
                ItemStack stack = screenHandler.getSlot(i).getStack();
                if (stack.getItem().toString().equals(savedSlot.getId())) {
                    InteractionHandler.swapStacks(savedSlot.getSlot(), i);
                    break;
                }
            }
        }
    }

    public static void delete(String name) {
        JsonObject allProfiles = FileHandler.getJsonFile(PROFILES_PATH);
        allProfiles.remove(name);
        FileHandler.write(PROFILES_PATH, allProfiles);
    }

    private static boolean equalNbt(NbtCompound stackNbt, NbtCompound savedSlotNbt) {
        boolean equalEnchantments = equalElements(stackNbt, savedSlotNbt, "Enchantments");
        boolean equalTrim = equalElements(stackNbt, savedSlotNbt, "Trim");
        return equalEnchantments == equalTrim;
    }

    private static boolean equalElements(NbtCompound stackNbt, NbtCompound savedSlotNbt, String elementName) {
        if (stackNbt.get(elementName) != null && savedSlotNbt.get(elementName) != null) {
            Set<NbtElement> saved_set = new HashSet<>(savedSlotNbt.getList(elementName, 10));
            Set<NbtElement> stack_set = new HashSet<>(stackNbt.getList(elementName, 10));
            return saved_set.equals(stack_set);
        }
        return stackNbt.get(elementName) == null && savedSlotNbt.get(elementName) == null;
    }
}
