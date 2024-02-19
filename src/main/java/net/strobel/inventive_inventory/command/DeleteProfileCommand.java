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
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.concurrent.CompletableFuture;

public class DeleteProfileCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-profile")
                .then(ClientCommandManager.literal("delete")
                        .then(ClientCommandManager.argument("profile", StringArgumentType.greedyString())
                                .suggests(DeleteProfileCommand::getProfiles)
                                .executes(DeleteProfileCommand::run)
                        )
                )
        );
    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        String profile = StringArgumentType.getString(context, "profile");
        ProfileHandler.delete(profile);
        return 1;
    }

    private static CompletableFuture<Suggestions> getProfiles(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH).keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }
}

