package net.strobel.inventive_inventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.handler.KeyInputHandler;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SaveProfileCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-profile")
                .then(ClientCommandManager.literal("save")
                        .then(ClientCommandManager.argument("name", StringArgumentType.word())
                                .executes(SaveProfileCommand::primary)
                                .then(ClientCommandManager.argument("keybind", StringArgumentType.word())
                                        .suggests(SaveProfileCommand::getKeyBinds)
                                        .executes(SaveProfileCommand::secondary)
                                )
                        )
                )
        );
    }

    private static int primary(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        ProfileHandler.save(name, "");
        return 1;
    }
    private static int secondary(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String keybind = StringArgumentType.getString(context, "keybind");
        ProfileHandler.save(name, keybind);
        return 1;
    }

    private static CompletableFuture<Suggestions> getKeyBinds(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        Arrays.stream(KeyInputHandler.profileKeys).forEach(keybind -> builder.suggest(keybind.getBoundKeyLocalizedText().getString()));
        return builder.buildFuture();
    }
}
