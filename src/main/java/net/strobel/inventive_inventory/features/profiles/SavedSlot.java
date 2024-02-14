package net.strobel.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.*;

class SavedSlot {
    private final int slot;
    private final String id;
    private final NbtCompound nbtData;

    public SavedSlot(int slot, String id, JsonObject nbtData) {
        this.slot = slot;
        this.id = id;
        this.nbtData = convertJsonObjectToNbt(nbtData);
    }

    public SavedSlot(int slot, String id, NbtCompound nbtData) {
        this.slot = slot;
        this.id = id;
        this.nbtData = convertRawNbtToNbt(nbtData);
    }

    private NbtCompound convertJsonObjectToNbt(JsonObject nbtData) {
        NbtCompound nbt = new NbtCompound();
        try {
            nbt.putString("custom_name", "\"" + nbtData.get("custom_name").getAsString() + "\"");
        } catch (IllegalStateException | NullPointerException ignored) {}

        try {
            JsonArray enchantmentsArray = nbtData.get("Enchantments").getAsJsonArray();
            NbtList enchantments = new NbtList();
            for (JsonElement enchantmentElement : enchantmentsArray) {
                JsonObject enchantmentObject = enchantmentElement.getAsJsonObject();
                NbtCompound enchantment = new NbtCompound();
                enchantment.putString("id", enchantmentObject.get("id").getAsString());
                enchantment.putShort("lvl", enchantmentObject.get("lvl").getAsShort());
                enchantments.add(enchantment);
            }
            nbt.put("Enchantments", enchantments);
        } catch (IllegalStateException | NullPointerException ignored) {
        }

        try {
            JsonObject trimObject = nbtData.get("Trim").getAsJsonObject();
            NbtCompound trim = new NbtCompound();
            trim.putString("material", trimObject.get("material").getAsString());
            trim.putString("pattern", trimObject.get("pattern").getAsString());
            nbt.put("Trim", trim);
        } catch (IllegalStateException | NullPointerException ignored) {
        }

        return nbt;
    }

    public JsonObject convertNbtToJsonObject() {
        JsonObject jsonObject = new JsonObject();

        if (this.nbtData.get("custom_name") != null) {
            jsonObject.addProperty("custom_name", this.nbtData.getString("custom_name"));
        }

        if (!this.nbtData.getList("Enchantments", 10).isEmpty()) {
            JsonArray enchantments = new JsonArray();
            NbtList enchantmentsNbtList = this.nbtData.getList("Enchantments", 10);
            for (NbtElement enchantmentElement : enchantmentsNbtList) {
                NbtCompound enchantmentCompound = (NbtCompound) enchantmentElement;
                JsonObject enchantment = new JsonObject();
                enchantment.addProperty("id", enchantmentCompound.getString("id"));
                enchantment.addProperty("lvl", enchantmentCompound.getShort("lvl"));
                enchantments.add(enchantment);
            }
            jsonObject.add("Enchantments", enchantments);
        }

        if (this.nbtData.get("Trim") != null) {
            JsonObject trim = new JsonObject();
            NbtCompound trimCompound = this.nbtData.getCompound("Trim");
            trim.addProperty("material", trimCompound.getString("material"));
            trim.addProperty("pattern", trimCompound.getString("pattern"));
            jsonObject.add("Trim", trim);
        }

        return jsonObject;
    }

    private NbtCompound convertRawNbtToNbt(NbtCompound nbtData) {
        NbtCompound nbt = new NbtCompound();
        if (nbtData == null) return nbt;

        if (nbtData.get("display") != null) {
            String customName = nbtData.getCompound("display").getString("Name");
            nbt.putString("custom_name", customName.substring(1, customName.length() - 1));
        }

        if (nbtData.get("Enchantments") != null) {
            NbtList enchantments = new NbtList();
            for (NbtElement rawEnchantment : nbtData.getList("Enchantments", 10)) {
                NbtCompound rawEnchantmentCompound = (NbtCompound) rawEnchantment;
                NbtCompound enchantment = new NbtCompound();
                enchantment.putString("id", rawEnchantmentCompound.getString("id"));
                enchantment.putShort("lvl", rawEnchantmentCompound.getShort("lvl"));
                enchantments.add(enchantment);
            }
            nbt.put("Enchantments", enchantments);
        }

        if (nbtData.get("Trim") != null) {
            NbtCompound rawTrim = nbtData.getCompound("Trim");
            if (!rawTrim.isEmpty()) {
                NbtCompound trim = new NbtCompound();
                trim.putString("material", rawTrim.getString("material"));
                trim.putString("pattern", rawTrim.getString("pattern"));
                nbt.put("Trim", trim);
            }
        }
        return nbt;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getId() {
        return this.id;
    }

    public NbtCompound getNbtData() {
        return this.nbtData;
    }
}
