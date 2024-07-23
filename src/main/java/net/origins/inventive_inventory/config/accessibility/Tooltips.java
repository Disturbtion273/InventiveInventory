package net.origins.inventive_inventory.config.accessibility;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.origins.inventive_inventory.InventiveInventory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Tooltips {
    private static final Identifier TOOLTIP_IDENTIFIER =Identifier.of(InventiveInventory.MOD_ID, "text/tooltips.json");

    public static Text STATUS = Text.empty();
    public static Text LOCKED_SLOTS_BEHAVIOUR = Text.empty();

    public static Text S_MODE = Text.empty();
    public static Text S_BEHAVIOUR = Text.empty();
    public static Text AR_MODE = Text.empty();
    public static Text AR_TOOL_BREAKING_BEHAVIOUR = Text.empty();
    public static Text AR_TOOL_BEHAVIOUR = Text.empty();
    public static Text P_LOAD_MODE = Text.empty();


    public static void init() {
        ResourceManager resourceManager = InventiveInventory.getClient().getResourceManager();
        if (resourceManager.getResource(TOOLTIP_IDENTIFIER).isEmpty()) return;
        try (InputStream stream = resourceManager.getResource(TOOLTIP_IDENTIFIER).get().getInputStream()) {
            JsonObject jsonObject = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
            STATUS = Text.of(jsonObject.get("STATUS").getAsString());
            LOCKED_SLOTS_BEHAVIOUR = Text.of(jsonObject.get("LOCKED_SLOTS_BEHAVIOUR").getAsString());
            S_MODE = Text.of(jsonObject.get("S_MODE").getAsString());
            S_BEHAVIOUR = Text.of(jsonObject.get("S_BEHAVIOUR").getAsString());
            AR_MODE = Text.of(jsonObject.get("AR_MODE").getAsString());
            AR_TOOL_BREAKING_BEHAVIOUR = Text.of(jsonObject.get("AR_TOOL_BREAKING_BEHAVIOUR").getAsString());
            AR_TOOL_BEHAVIOUR = Text.of(jsonObject.get("AR_TOOL_BEHAVIOUR").getAsString());
            P_LOAD_MODE = Text.of(jsonObject.get("P_LOAD_MODE").getAsString());
        } catch (IOException ignored) {}
    }
}
