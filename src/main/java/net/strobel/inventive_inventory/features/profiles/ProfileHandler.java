package net.strobel.inventive_inventory.features.profiles;

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
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.keybindfix.MixinIKeyBindingDisplay;
import net.strobel.inventive_inventory.slots.PlayerSlots;
import net.strobel.inventive_inventory.util.FileHandler;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProfileHandler {
    private static final String PROFILES_FILE = "profiles.json";
    public static final Path PROFILES_PATH = ConfigManager.PATH.resolve(PROFILES_FILE);
    public static List<String> profileNames = new ArrayList<>();

    private static final Style style = Style.EMPTY.withBold(true);

    public static void initialize() {
        int i = 1;
        JsonObject allProfiles = FileHandler.getJsonFile(PROFILES_PATH);
        for (KeyBinding profileKey : KeyInputHandler.profileKeys) {
            String displayName = "Profile " + i;
            for (String profileName : allProfiles.keySet()) {
                String keybind = allProfiles.getAsJsonObject(profileName).get("keybind").getAsString();
                if (keybind.equals(profileKey.getBoundKeyLocalizedText().getString())) {
                    displayName = profileName;
                    break;
                }
            }
            ((MixinIKeyBindingDisplay) profileKey).main$setDisplayName(displayName);
            profileNames.add(displayName);
            i++;
        }
    }

    public static void save(String name, String key) {
        JsonObject allProfiles = FileHandler.getJsonFile(PROFILES_PATH);
        List<String> allKeybinds = allProfiles.keySet().stream()
                .map(profileName -> allProfiles.getAsJsonObject(profileName).get("keybind").getAsString())
                .filter(keybind -> !keybind.isBlank())
                .toList();

        if (allProfiles.has(name) || allKeybinds.contains(key)) {
            Profile.overwrite(name, key);
        } else {
            Profile.create(name, key);
        }
        Text text = Text.of("Saved Profile: " + name).copy().setStyle(style.withColor(Formatting.GREEN));
        InventiveInventory.getPlayer().sendMessage(text, true);
    }

    public static void load(String name) {
        Text text;
        if (FileHandler.getJsonFile(PROFILES_PATH).has(name)) {
            Profile profile = Profile.load(name);
            List<SavedSlot> savedSlots = profile.getSavedSlots();
            ScreenHandler screenHandler = InventiveInventory.getScreenHandler();

            Sorter.mergeItemStacks(PlayerSlots.getWithHotbar().excludeLockedSlots(), screenHandler);

            for (SavedSlot savedSlot : savedSlots) {
                boolean matchFound = false;

                for (int i : PlayerSlots.getWithHotbarAndArmor().excludeLockedSlots()) {
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

                for (int i : PlayerSlots.getWithHotbarAndArmor().excludeLockedSlots()) {
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

                for (int i : PlayerSlots.getWithHotbarAndArmor().excludeLockedSlots()) {
                    ItemStack stack = screenHandler.getSlot(i).getStack();
                    if (stack.getItem().toString().equals(savedSlot.getId())) {
                        InteractionHandler.swapStacks(savedSlot.getSlot(), i);
                        break;
                    }
                }
            }
            text = Text.of("Loaded Profile: " + name).copy().setStyle(style.withColor(Formatting.GREEN));
        } else {
            text = Text.of("Profile '" + name + "' not found!").copy().setStyle(style.withColor(Formatting.RED));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
    }

    public static void delete(String name, boolean sendMessage) {
        Text text;
        JsonObject allProfiles = FileHandler.getJsonFile(PROFILES_PATH);

        if (allProfiles.has(name)) {
            String keybind = allProfiles.getAsJsonObject(name).get("keybind").getAsString();
            for (KeyBinding keyBinding : KeyInputHandler.profileKeys) {
                if (keyBinding.getBoundKeyLocalizedText().getString().equals(keybind)) {
                    ((MixinIKeyBindingDisplay) keyBinding).main$resetDisplayName();
                    break;
                }
            }
            allProfiles.remove(name);
            FileHandler.write(PROFILES_PATH, allProfiles);
            int index = ProfileHandler.profileNames.indexOf(name);
            try {
                ProfileHandler.profileNames.set(index, "Profile " + (index + 1));
            } catch (IndexOutOfBoundsException ignored) {}
            text = Text.of("Deleted: " + name).copy().setStyle(style.withColor(Formatting.GREEN));
        } else {
            text = Text.of("Profile '" + name + "' not found!").copy().setStyle(style.withColor(Formatting.RED));
        }
        if (sendMessage) {
            InventiveInventory.getPlayer().sendMessage(text, true);
        }
    }

    public static void overwrite(String name, String key) {
        JsonObject allProfiles = FileHandler.getJsonFile(PROFILES_PATH);
        if (allProfiles.has(name)) {
            Profile.overwrite(name, key);
        }
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
