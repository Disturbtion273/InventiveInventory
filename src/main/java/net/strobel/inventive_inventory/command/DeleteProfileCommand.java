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
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.util.FileHandler;

import java.util.List;
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
        List<String> profiles = FileHandler.getJsonFile(ProfileHandler.PROFILES_PATH).keySet().stream().toList();
        Text text;
        if (profiles.contains(profile)) {
            ProfileHandler.delete(profile);
            text = Text.of("Deleted: " + profile).copy().setStyle(Style.EMPTY.withColor(Formatting.GREEN).withBold(true));
        } else {
            text = Text.of("Profile '" + profile + "' not found!").copy().setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
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
