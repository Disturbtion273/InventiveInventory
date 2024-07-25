package net.origins.inventive_inventory.commands.automatic_refilling;

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
import net.origins.inventive_inventory.config.enums.automatic_refilling.*;
import net.origins.inventive_inventory.util.Notifier;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class AutomaticRefillingConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-config")
                .then(ClientCommandManager.literal("AutomaticRefilling")
                        .then(ClientCommandManager.literal("set")
                                .then(ClientCommandManager.literal("Status")
                                        .then(ClientCommandManager.argument("status", StringArgumentType.word())
                                                .suggests(AutomaticRefillingConfigCommand::getStatuses)
                                                .executes(AutomaticRefillingConfigCommand::switchStatus)
                                        )
                                )
                                .then(ClientCommandManager.literal("Mode")
                                        .then(ClientCommandManager.argument("mode", StringArgumentType.word())
                                                .suggests(AutomaticRefillingConfigCommand::getModes)
                                                .executes(AutomaticRefillingConfigCommand::switchMode)
                                        )
                                )
                                .then(ClientCommandManager.literal("ToolBreakingBehaviour")
                                        .then(ClientCommandManager.argument("toolBreakingBehaviour", StringArgumentType.greedyString())
                                                .suggests(AutomaticRefillingConfigCommand::getToolBreakingBehaviours)
                                                .executes(AutomaticRefillingConfigCommand::switchToolBreakingBehaviour)
                                        )
                                )
                                .then(ClientCommandManager.literal("ToolBehaviour")
                                        .then(ClientCommandManager.argument("toolBehaviour", StringArgumentType.word())
                                                .suggests(AutomaticRefillingConfigCommand::getToolBehaviours)
                                                .executes(AutomaticRefillingConfigCommand::switchToolBehaviour)
                                        )
                                )
                                .then(ClientCommandManager.literal("LockedSlotsBehaviour")
                                        .then(ClientCommandManager.argument("lockedSlotsBehaviour", StringArgumentType.word())
                                                .suggests(AutomaticRefillingConfigCommand::getLockedSlotsBehaviours)
                                                .executes(AutomaticRefillingConfigCommand::switchLockedSlotsBehaviour)
                                        )
                                )
                        )
                        .then(ClientCommandManager.literal("get")
                                .then(ClientCommandManager.literal("Status")
                                        .executes(AutomaticRefillingConfigCommand::getStatus)
                                )
                                .then(ClientCommandManager.literal("Mode")
                                        .executes(AutomaticRefillingConfigCommand::getMode)
                                )
                                .then(ClientCommandManager.literal("ToolBreakingBehaviour")
                                        .executes(AutomaticRefillingConfigCommand::getToolBreakingBehaviour)
                                )
                                .then(ClientCommandManager.literal("ToolBehaviour")
                                        .executes(AutomaticRefillingConfigCommand::getToolBehaviour)
                                )
                                .then(ClientCommandManager.literal("LockedSlotsBehaviour")
                                        .executes(AutomaticRefillingConfigCommand::getLockedSlotsBehaviour)
                                )
                        )
                )
        );
    }

    private static CompletableFuture<Suggestions> getStatuses(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(AutomaticRefillingStatus.values()).forEach(status -> builder.suggest(status.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getModes(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(AutomaticRefillingModes.values()).forEach(mode -> builder.suggest(mode.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getToolBreakingBehaviours(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(AutomaticRefillingToolBreakingBehaviour.values()).forEach(behaviour -> builder.suggest(behaviour.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getToolBehaviours(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(AutomaticRefillingToolBehaviours.values()).forEach(behaviour -> builder.suggest(behaviour.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getLockedSlotsBehaviours(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(AutomaticRefillingLockedSlotsBehaviours.values()).forEach(behaviour -> builder.suggest(behaviour.getName()));
        return builder.buildFuture();
    }

    private static int switchStatus(CommandContext<FabricClientCommandSource> context) {
        String status = StringArgumentType.getString(context, "status");
        if (Arrays.stream(AutomaticRefillingStatus.values()).anyMatch(s -> s.getName().equals(status))) {
            ConfigManager.AUTOMATIC_REFILLING = AutomaticRefillingStatus.valueOf(status.toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Status to: " + status, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Status to: " + status);
        return -1;
    }

    private static int switchMode(CommandContext<FabricClientCommandSource> context) {
        String mode = StringArgumentType.getString(context, "mode");
        if (Arrays.stream(AutomaticRefillingModes.values()).anyMatch(s -> s.getName().equals(mode))) {
            ConfigManager.AR_MODE = AutomaticRefillingModes.valueOf(mode.toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Mode to: " + mode, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Mode to: " + mode);
        return -1;
    }

    private static int switchToolBreakingBehaviour(CommandContext<FabricClientCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "toolBreakingBehaviour");
        if (Arrays.stream(AutomaticRefillingToolBreakingBehaviour.values()).anyMatch(s -> s.getName().equals(behaviour))) {
            ConfigManager.AR_TOOL_BREAKING_BEHAVIOUR = AutomaticRefillingToolBreakingBehaviour.valueOf(behaviour.replaceAll("[ -]", "_").toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Tool Breaking Behaviour to: " + behaviour, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Tool Breaking Behaviour to: " + behaviour);
        return -1;
    }

    private static int switchToolBehaviour(CommandContext<FabricClientCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "toolBehaviour");
        if (Arrays.stream(AutomaticRefillingToolBehaviours.values()).anyMatch(s -> s.getName().equals(behaviour))) {
            ConfigManager.AR_TOOL_BEHAVIOUR = AutomaticRefillingToolBehaviours.valueOf(behaviour.toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Tool Behaviour to: " + behaviour, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Tool Behaviour to: " + behaviour);
        return -1;
    }

    private static int switchLockedSlotsBehaviour(CommandContext<FabricClientCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "lockedSlotsBehaviour");
        if (Arrays.stream(AutomaticRefillingLockedSlotsBehaviours.values()).anyMatch(s -> s.getName().equals(behaviour))) {
            ConfigManager.AR_LS_BEHAVIOUR = AutomaticRefillingLockedSlotsBehaviours.valueOf(behaviour.toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Locked Slots Behaviour to: " + behaviour, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Locked Slots Behaviour to: " + behaviour);
        return -1;
    }

    private static int getStatus(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Status: " + ConfigManager.AUTOMATIC_REFILLING.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getMode(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Mode: " + ConfigManager.AR_MODE.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getToolBreakingBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Tool Breaking Behaviour: " + ConfigManager.AR_TOOL_BREAKING_BEHAVIOUR.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getToolBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Tool Behaviour: " + ConfigManager.AR_TOOL_BEHAVIOUR.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getLockedSlotsBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Locked Slots Behaviour: " + ConfigManager.AR_LS_BEHAVIOUR.getName(), Formatting.BLUE);
        return 1;
    }
}
