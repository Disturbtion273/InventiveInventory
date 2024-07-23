package net.origins.inventive_inventory.commands.profiles;

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
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;

import java.util.concurrent.CompletableFuture;

public class ProfilesLoadCommand {
    private static final Style style = Style.EMPTY.withBold(true);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-profiles")
                .then(ClientCommandManager.literal("load")
                        .then(ClientCommandManager.argument("profile", StringArgumentType.greedyString())
                                .suggests(ProfilesLoadCommand::getProfiles)
                                .executes(ProfilesLoadCommand::load)
                        )
                )
        );
    }

    private static int load(CommandContext<FabricClientCommandSource> context) {
        String profileArg = StringArgumentType.getString(context, "profile");
        for (Profile profile : ProfileHandler.getProfiles()) {
            if (!profileArg.isEmpty() && profile.getName().equals(profileArg)) {
                ProfileHandler.load(profile);
                return 1;
            }
        }
        Text text = Text.of("This profile does not exist!").copy().setStyle(style.withColor(Formatting.RED));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return -1;
    }

    private static CompletableFuture<Suggestions> getProfiles(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        ProfileHandler.getProfiles().forEach(profile -> {
            if (!profile.getName().isEmpty()) builder.suggest(profile.getName());
        });
        return builder.buildFuture();
    }
}
