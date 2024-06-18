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
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ProfilesConfigCommand {
    private static final Map<String, Mode> MODE_MAP = Map.of("Standard", Mode.STANDARD, "Fast Load", Mode.FAST_LOAD);
    private static final Style style = Style.EMPTY.withBold(true);

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-config")
                .then(ClientCommandManager.literal("set")
                        .then(ClientCommandManager.literal("ProfileFastLoadingMode")
                                .then(ClientCommandManager.argument("profile", StringArgumentType.word())
                                        .suggests(ProfilesConfigCommand::getProfiles)
                                        .then(ClientCommandManager.argument("mode", StringArgumentType.greedyString())
                                                .suggests(ProfilesConfigCommand::getModes)
                                                .executes(ProfilesConfigCommand::setMode)
                                        )
                                )
                        )
                )
                .then(ClientCommandManager.literal("get")
                        .then(ClientCommandManager.literal("ProfileFastLoadingMode")
                                .then(ClientCommandManager.argument("profile", StringArgumentType.word())
                                        .suggests(ProfilesConfigCommand::getProfiles)
                                        .executes(ProfilesConfigCommand::getMode)
                                )


                        )
                )
        );
    }

    private static int setMode(CommandContext<FabricClientCommandSource> context) {
        String profile = StringArgumentType.getString(context, "profile").replace("-", " ");
        String mode = StringArgumentType.getString(context, "mode");
        Text text = Text.of("Profile '" + profile + "' not found!").copy().setStyle(style.withColor(Formatting.RED));
        if (!MODE_MAP.containsKey(mode)) {
            text = Text.of("You cannot switch the mode to: " + mode).copy().setStyle(style.withColor(Formatting.RED));
        } else if (ProfileHandler.profileNames.contains(profile)) {
            ConfigManager.PROFILE_FAST_LOADING.set(ProfileHandler.profileNames.indexOf(profile), MODE_MAP.get(mode));
            ConfigManager.save();
            text = Text.of("Switched Mode for " + profile + " to: " + mode).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getMode(CommandContext<FabricClientCommandSource> context) {
        String profile = StringArgumentType.getString(context, "profile").replace("-", " ");
        Text text = Text.of("Profile '" + profile + "' not found!").copy().setStyle(style.withColor(Formatting.RED));
        if (ProfileHandler.profileNames.contains(profile)) {
            text = Text.of("Mode for " + profile + ": " + ConfigManager.PROFILE_FAST_LOADING.get(ProfileHandler.profileNames.indexOf(profile)).toString()).copy().setStyle(style.withColor(Formatting.BLUE));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static CompletableFuture<Suggestions> getProfiles(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        ProfileHandler.profileNames.forEach(profile -> {
            String profileWithHyphen = profile.replace(" ", "-");
            builder.suggest(profileWithHyphen);
        });
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getModes(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        MODE_MAP.keySet().forEach(builder::suggest);
        return builder.buildFuture();
    }
}
