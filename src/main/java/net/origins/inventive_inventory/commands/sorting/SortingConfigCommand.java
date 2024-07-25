package net.origins.inventive_inventory.commands.sorting;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.util.Formatting;
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.sorting.SortingBehaviours;
import net.origins.inventive_inventory.config.enums.sorting.SortingModes;
import net.origins.inventive_inventory.config.enums.sorting.SortingStatus;
import net.origins.inventive_inventory.util.Notifier;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class SortingConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-config")
                .then(ClientCommandManager.literal("Sorting")
                        .then(ClientCommandManager.literal("set")
                                .then(ClientCommandManager.literal("Status")
                                        .then(ClientCommandManager.argument("status", StringArgumentType.word())
                                                .suggests(SortingConfigCommand::getStatuses)
                                                .executes(SortingConfigCommand::switchStatus)
                                        )
                                )
                                .then(ClientCommandManager.literal("Mode")
                                        .then(ClientCommandManager.argument("mode", StringArgumentType.greedyString())
                                                .suggests(SortingConfigCommand::getModes)
                                                .executes(SortingConfigCommand::switchMode)
                                        )
                                )
                                .then(ClientCommandManager.literal("Behaviour")
                                        .then(ClientCommandManager.argument("behaviour", StringArgumentType.greedyString())
                                                .suggests(SortingConfigCommand::getBehaviours)
                                                .executes(SortingConfigCommand::switchBehaviour)
                                        )
                                )
                        )
                        .then(ClientCommandManager.literal("get")
                                .then(ClientCommandManager.literal("Status")
                                        .executes(SortingConfigCommand::getStatus)
                                )
                                .then(ClientCommandManager.literal("Mode")
                                        .executes(SortingConfigCommand::getMode)
                                )
                                .then(ClientCommandManager.literal("Behaviour")
                                        .executes(SortingConfigCommand::getBehaviour)
                                )
                        )
                )
        );
    }

    private static CompletableFuture<Suggestions> getStatuses(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(SortingStatus.values()).forEach(status -> builder.suggest(status.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getModes(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(SortingModes.values()).forEach(mode -> builder.suggest(mode.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getBehaviours(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(SortingBehaviours.values()).forEach(behaviour -> builder.suggest(behaviour.getName()));
        return builder.buildFuture();
    }

    private static int switchStatus(CommandContext<FabricClientCommandSource> context) {
        String status = StringArgumentType.getString(context, "status");
        if (Arrays.stream(SortingStatus.values()).anyMatch(s -> s.getName().equals(status))) {
            ConfigManager.SORTING = SortingStatus.valueOf(status.toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Status to: " + status, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Status to: " + status);
        return -1;
    }

    private static int switchMode(CommandContext<FabricClientCommandSource> context) {
        String mode = StringArgumentType.getString(context, "mode");
        if (Arrays.stream(SortingModes.values()).anyMatch(s -> s.getName().equals(mode))) {
            ConfigManager.S_MODE = SortingModes.valueOf(mode.replaceAll("[ -]", "_").toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Mode to: " + mode, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the mode to: " + mode);
        return -1;
    }

    private static int switchBehaviour(CommandContext<FabricClientCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "behaviour");
        if (Arrays.stream(SortingBehaviours.values()).anyMatch(s -> s.getName().equals(behaviour))) {
            ConfigManager.S_BEHAVIOUR = SortingBehaviours.valueOf(behaviour.replaceAll("[ -]", "_").toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Behaviour to: " + behaviour, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Behaviour to: " + behaviour);
        return -1;
    }

    private static int getStatus(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Status: " + ConfigManager.SORTING.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getMode(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Mode: " + ConfigManager.S_MODE.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Behaviour: " + ConfigManager.S_BEHAVIOUR.getName(), Formatting.BLUE);
        return 1;
    }
}
