package net.strobel.inventive_inventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LoadProfileCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventiveprofile")
                .then(ClientCommandManager.literal("load")
                .then(ClientCommandManager.argument("profile", StringArgumentType.word())
                        .suggests(LoadProfileCommand::getProfiles)
                        .executes(LoadProfileCommand::run))));
    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        String profile = StringArgumentType.getString(context, "profile");
        List<String> profiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH).keySet().stream().toList();
        if (profiles.contains(profile)) {
            context.getSource().sendFeedback(Text.of("Loading successfully: " + profile));
        } else {
            context.getSource().sendFeedback(Text.of("Profile " + profile + " not found!"));
        }
        return 1;
    }

    private static CompletableFuture<Suggestions> getProfiles(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        String[] profiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH).keySet().toArray(new String[0]);

        for (String profile : profiles) {
            builder.suggest(profile);
        }
        return builder.buildFuture();
    }
}
