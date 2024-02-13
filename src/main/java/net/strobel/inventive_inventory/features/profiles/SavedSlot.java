package net.strobel.inventive_inventory.features.profiles;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.*;

public class SavedSlot {
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
            nbt.putString("custom_name", nbtData.get("custom_name").getAsString());
        } catch (IllegalStateException ignored) {
        }

        try {
            JsonArray enchantmentsArray = nbtData.get("Enchantments").getAsJsonArray();
            NbtList enchantments = new NbtList();
            for (JsonElement enchantmentElement : enchantmentsArray) {
                JsonObject enchantmentObject = enchantmentElement.getAsJsonObject();
                NbtCompound enchantment = new NbtCompound();
                enchantment.putString("id", enchantmentObject.getAsString());
                enchantment.putShort("lvl", enchantmentObject.getAsShort());
                enchantments.add(enchantment);
            }
            nbt.put("Enchantments", enchantments);
        } catch (IllegalStateException ignored) {}

        try {
            JsonObject trimObject = nbtData.get("Trim").getAsJsonObject();
            NbtCompound trim = new NbtCompound();
            trim.putString("material", trimObject.get("material").getAsString());
            trim.putString("pattern", trimObject.get("pattern").getAsString());
            nbt.put("Trim", trim);
        } catch (IllegalStateException ignored) {
        }

        return nbt;
    }

    public JsonObject convertNbtToJsonObject() {
        JsonObject jsonObject = new JsonObject();

        //if (this.nbtData.get("custom_name") != null) {
        try {
            jsonObject.addProperty("custom_name", this.nbtData.get("custom_name").toString());
        } catch (NullPointerException ignored) {}
        //}

        if (!this.nbtData.getList("Enchantments", 10).isEmpty()) {
            try {
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
            } catch (NullPointerException ignored) {}
        }

        //if (this.nbtData.getCompound("Trim") != null) {
        try {
            JsonObject trim = new JsonObject();
            NbtCompound trimCompound = this.nbtData.getCompound("Trim");
            trim.addProperty("material", trimCompound.get("material").toString());
            trim.addProperty("pattern", trimCompound.get("pattern").toString());
            jsonObject.add("Trim", trim);
        } catch (NullPointerException ignored) {}
        //}
        return jsonObject;
    }

    private NbtCompound convertRawNbtToNbt (NbtCompound nbtData) {
        NbtCompound nbt = new NbtCompound();
        if (nbtData == null) {return nbt;}
        try {
            String customName = "";
            customName = nbtData.getCompound("display").get("Name").asString();
            nbt.putString("custom_name", customName);
        } catch (NullPointerException ignored) {}

        try {
            NbtList enchantments = new NbtList();
            NbtList rawEnchantments = nbtData.getList("Enchantments", 10);
            for (NbtElement rawEnchantment: rawEnchantments) {
                NbtCompound rawEnchantmentCompound = (NbtCompound) rawEnchantment;
                NbtCompound enchantment = new NbtCompound();
                enchantment.putString("id", rawEnchantmentCompound.getString("id"));
                enchantment.putShort("lvl", rawEnchantmentCompound.getShort("lvl"));
                enchantments.add(enchantment);
            }
            nbt.put("Enchantments", enchantments);
        } catch (NullPointerException ignored) {}

        try {
            NbtCompound rawTriM = nbtData.getCompound("Trim");
            if (!rawTriM.isEmpty()) {
                NbtCompound trim = new NbtCompound();
                trim.putString("material", rawTriM.getString("material"));
                trim.putString("pattern", rawTriM.getString("pattern"));
                nbt.put("Trim", trim);
            }
        } catch (NullPointerException ignored) {}

        return nbt;
    }

    public int getSlot() {
        return this.slot;
    }

    public String getId() {
        return this.id;
    }

    public NbtCompound getNbtData() { return  this.nbtData; }
}
