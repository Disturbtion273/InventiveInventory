package net.strobel.inventive_inventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.config.ConfigManager;
import net.strobel.inventive_inventory.config.Mode;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class SortingConfigCommand {
    private static final Map<String, Mode> MODE_MAP = Map.of("Name", Mode.NAME, "Item Type", Mode.ITEM_TYPE);
    private static final Style style = Style.EMPTY.withBold(true);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-config")
                .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.literal("SortingMode")
                                .then(ClientCommandManager.argument("mode", StringArgumentType.greedyString())
                                        .suggests(SortingConfigCommand::getModes)
                                        .executes(SortingConfigCommand::switchMode)
                                )
                        )
                )
                .then(ClientCommandManager.literal("get")
                        .then(ClientCommandManager.literal("SortingMode")
                                .executes(SortingConfigCommand::getMode)
                        )
                )
        );
    }

    private static int switchMode(CommandContext<FabricClientCommandSource> context) {
        String mode = StringArgumentType.getString(context, "mode");
        Text text = Text.of("You cannot switch the mode to: " + mode).copy().setStyle(style.withColor(Formatting.RED));
        if (MODE_MAP.containsKey(mode)) {
            ConfigManager.SORTING = MODE_MAP.get(mode);
            ConfigManager.save();
            text = Text.of("Switched Mode to: " + mode).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getMode(CommandContext<FabricClientCommandSource> context) {
        Text text = Text.of("Mode: " + ConfigManager.SORTING.toString()).copy().setStyle(style.withColor(Formatting.BLUE));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static CompletableFuture<Suggestions> getModes(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        MODE_MAP.keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }
}
