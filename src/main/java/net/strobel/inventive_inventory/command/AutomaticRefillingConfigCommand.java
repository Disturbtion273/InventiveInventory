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

public class AutomaticRefillingConfigCommand {
    private static final Map<String, Mode> MODE_MAP = Map.of("Standard", Mode.STANDARD, "Inverted", Mode.INVERTED);
    private static final Style style = Style.EMPTY.withBold(true);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-config")
                .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.literal("AutomaticRefillingMode")
                                .then(ClientCommandManager.argument("mode", StringArgumentType.word())
                                        .suggests(AutomaticRefillingConfigCommand::getModes)
                                        .executes(AutomaticRefillingConfigCommand::switchMode)
                                )
                        )
                )
                .then(ClientCommandManager.literal("get")
                        .then(ClientCommandManager.literal("AutomaticRefillingMode")
                                .executes(AutomaticRefillingConfigCommand::getMode)
                        )
                )
        );
    }

    private static int switchMode(CommandContext<FabricClientCommandSource> context) {
        String mode = StringArgumentType.getString(context, "mode");
        Text text = Text.of("You cannot switch the mode to: " + mode).copy().setStyle(style.withColor(Formatting.RED));
        if (MODE_MAP.containsKey(mode)) {
            ConfigManager.AUTOMATIC_REFILLING = MODE_MAP.get(mode);
            ConfigManager.save();
            text = Text.of("Switched Mode to: " + mode).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getMode(CommandContext<FabricClientCommandSource> context) {
        Text text = Text.of("Mode: " + ConfigManager.AUTOMATIC_REFILLING.toString()).copy().setStyle(style.withColor(Formatting.BLUE));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static CompletableFuture<Suggestions> getModes(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        MODE_MAP.keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }
}
