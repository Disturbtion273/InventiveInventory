package net.origins.inventive_inventory.commands.automatic_refilling;

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
import net.origins.inventive_inventory.config.ConfigManager;
import net.origins.inventive_inventory.config.enums.automatic_refilling.*;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class AutomaticRefillingConfigCommand {
    private static final Style style = Style.EMPTY.withBold(true);

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
        Text text = Text.of("You cannot switch the Status to: " + status).copy().setStyle(style.withColor(Formatting.RED));
        if (Arrays.stream(AutomaticRefillingStatus.values()).anyMatch(s -> s.getName().equals(status))) {
            ConfigManager.AUTOMATIC_REFILLING = AutomaticRefillingStatus.valueOf(status.toUpperCase());
            ConfigManager.save();
            text = Text.of("Switched Status to: " + status).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int switchMode(CommandContext<FabricClientCommandSource> context) {
        String mode = StringArgumentType.getString(context, "mode");
        Text text = Text.of("You cannot switch the mode to: " + mode).copy().setStyle(style.withColor(Formatting.RED));
        if (Arrays.stream(AutomaticRefillingModes.values()).anyMatch(s -> s.getName().equals(mode))) {
            ConfigManager.AR_MODE = AutomaticRefillingModes.valueOf(mode.toUpperCase());
            ConfigManager.save();
            text = Text.of("Switched Mode to: " + mode).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int switchToolBreakingBehaviour(CommandContext<FabricClientCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "toolBreakingBehaviour");
        Text text = Text.of("You cannot switch the Tool Breaking Behaviour to: " + behaviour).copy().setStyle(style.withColor(Formatting.RED));
        if (Arrays.stream(AutomaticRefillingToolBreakingBehaviour.values()).anyMatch(s -> s.getName().equals(behaviour))) {
            ConfigManager.AR_TOOL_BREAKING_BEHAVIOUR = AutomaticRefillingToolBreakingBehaviour.valueOf(behaviour.replaceAll("[ -]", "_").toUpperCase());
            ConfigManager.save();
            text = Text.of("Switched Tool Breaking Behaviour to: " + behaviour).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int switchToolBehaviour(CommandContext<FabricClientCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "toolBehaviour");
        Text text = Text.of("You cannot switch the Tool Behaviour to: " + behaviour).copy().setStyle(style.withColor(Formatting.RED));
        if (Arrays.stream(AutomaticRefillingToolBehaviours.values()).anyMatch(s -> s.getName().equals(behaviour))) {
            ConfigManager.AR_TOOL_BEHAVIOUR = AutomaticRefillingToolBehaviours.valueOf(behaviour.toUpperCase());
            ConfigManager.save();
            text = Text.of("Switched Tool Behaviour to: " + behaviour).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int switchLockedSlotsBehaviour(CommandContext<FabricClientCommandSource> context) {
        String behaviour = StringArgumentType.getString(context, "lockedSlotsBehaviour");
        Text text = Text.of("You cannot switch the Locked Slots Behaviour to: " + behaviour).copy().setStyle(style.withColor(Formatting.RED));
        if (Arrays.stream(AutomaticRefillingLockedSlotsBehaviours.values()).anyMatch(s -> s.getName().equals(behaviour))) {
            ConfigManager.AR_LS_BEHAVIOUR = AutomaticRefillingLockedSlotsBehaviours.valueOf(behaviour.toUpperCase());
            ConfigManager.save();
            text = Text.of("Switched Locked Slots Behaviour to: " + behaviour).copy().setStyle(style.withColor(Formatting.GREEN));
        }
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getStatus(CommandContext<FabricClientCommandSource> ignoredContext) {
        Text text = Text.of("Status: " + ConfigManager.AUTOMATIC_REFILLING.getName()).copy().setStyle(style.withColor(Formatting.BLUE));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getMode(CommandContext<FabricClientCommandSource> ignoredContext) {
        Text text = Text.of("Mode: " + ConfigManager.AR_MODE.getName()).copy().setStyle(style.withColor(Formatting.BLUE));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getToolBreakingBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Text text = Text.of("Tool Breaking Behaviour: " + ConfigManager.AR_TOOL_BREAKING_BEHAVIOUR.getName()).copy().setStyle(style.withColor(Formatting.BLUE));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getToolBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Text text = Text.of("Tool Behaviour: " + ConfigManager.AR_TOOL_BEHAVIOUR.getName()).copy().setStyle(style.withColor(Formatting.BLUE));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }

    private static int getLockedSlotsBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Text text = Text.of("Locked Slots Behaviour: " + ConfigManager.AR_LS_BEHAVIOUR.getName()).copy().setStyle(style.withColor(Formatting.BLUE));
        InventiveInventory.getPlayer().sendMessage(text, true);
        return 1;
    }
}
