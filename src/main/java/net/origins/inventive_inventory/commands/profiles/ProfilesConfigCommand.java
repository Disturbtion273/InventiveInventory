package net.origins.inventive_inventory.commands.profiles;

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
import net.origins.inventive_inventory.config.enums.profiles.ProfilesLoadMode;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesLockedSlotsBehaviours;
import net.origins.inventive_inventory.config.enums.profiles.ProfilesStatus;
import net.origins.inventive_inventory.features.profiles.gui.ProfilesConfigScreen;
import net.origins.inventive_inventory.util.Notifier;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public class ProfilesConfigCommand {

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventive-config")
                .then(ClientCommandManager.literal("Profiles")
                        .then(ClientCommandManager.literal("set")
                                .then(ClientCommandManager.literal("Status")
                                        .then(ClientCommandManager.argument("status", StringArgumentType.word())
                                                .suggests(ProfilesConfigCommand::getStatuses)
                                                .executes(ProfilesConfigCommand::switchStatus)
                                        )
                                )
                                .then(ClientCommandManager.literal("LoadMode")
                                        .then(ClientCommandManager.argument("loadMode", StringArgumentType.greedyString())
                                                .suggests(ProfilesConfigCommand::getLoadModes)
                                                .executes(ProfilesConfigCommand::switchLoadMode)
                                        )
                                )
                                .then(ClientCommandManager.literal("LockedSlotsBehaviour")
                                        .then(ClientCommandManager.argument("lockedSlotsBehaviour", StringArgumentType.word())
                                                .suggests(ProfilesConfigCommand::getLockedSlotsBehaviours)
                                                .executes(ProfilesConfigCommand::switchLockedSlotsBehaviour)
                                        )
                                )
                        )
                        .then(ClientCommandManager.literal("get")
                                .then(ClientCommandManager.literal("Status")
                                        .executes(ProfilesConfigCommand::getStatus)
                                )
                                .then(ClientCommandManager.literal("LoadMode")
                                        .executes(ProfilesConfigCommand::getLoadMode)
                                )
                                .then(ClientCommandManager.literal("LockedSlotsBehaviour")
                                        .executes(ProfilesConfigCommand::getLockedSlotsBehaviour)
                                )
                        )
                        .then(ClientCommandManager.literal("ConfigScreen")
                                .executes(ProfilesConfigCommand::setConfigScreen)
                        )
                )
        );
    }

    private static CompletableFuture<Suggestions> getStatuses(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(ProfilesStatus.values()).forEach(status -> builder.suggest(status.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getLoadModes(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(ProfilesLoadMode.values()).forEach(mode -> builder.suggest(mode.getName()));
        return builder.buildFuture();
    }

    private static CompletableFuture<Suggestions> getLockedSlotsBehaviours(CommandContext<FabricClientCommandSource> ignoredContext, SuggestionsBuilder builder) {
        Arrays.asList(ProfilesLockedSlotsBehaviours.values()).forEach(behaviour -> builder.suggest(behaviour.getName()));
        return builder.buildFuture();
    }

    private static int switchStatus(CommandContext<FabricClientCommandSource> context) {
        String status = StringArgumentType.getString(context, "status");
        if (Arrays.stream(ProfilesStatus.values()).anyMatch(s -> s.getName().equals(status))) {
            ConfigManager.PROFILES = ProfilesStatus.valueOf(status.toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Status to: " + status, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Status to: " + status);
        return -1;
    }

    private static int switchLoadMode(CommandContext<FabricClientCommandSource> context) {
        String loadMode = StringArgumentType.getString(context, "loadMode");
        if (Arrays.stream(ProfilesLoadMode.values()).anyMatch(s -> s.getName().equals(loadMode))) {
            ConfigManager.P_LOAD_MODE = ProfilesLoadMode.valueOf(loadMode.replaceAll("[ -]", "_").toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Load Mode to: " + loadMode, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Load Mode to: " + loadMode);
        return -1;
    }

    private static int switchLockedSlotsBehaviour(CommandContext<FabricClientCommandSource> context) {
        String lockedSlotsBehaviour = StringArgumentType.getString(context, "lockedSlotsBehaviour");
        if (Arrays.stream(ProfilesLockedSlotsBehaviours.values()).anyMatch(s -> s.getName().equals(lockedSlotsBehaviour))) {
            ConfigManager.P_LS_BEHAVIOUR = ProfilesLockedSlotsBehaviours.valueOf(lockedSlotsBehaviour.toUpperCase());
            ConfigManager.save();
            Notifier.send("Switched Locked Slots Behaviour to: " + lockedSlotsBehaviour, Formatting.GREEN);
            return 1;
        }
        Notifier.error("You cannot switch the Locked Slots Behaviour to: " + lockedSlotsBehaviour);
        return -1;
    }

    private static int getStatus(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Status: " + ConfigManager.PROFILES.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getLoadMode(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Load Mode: " + ConfigManager.P_LOAD_MODE.getName(), Formatting.BLUE);
        return 1;
    }

    private static int getLockedSlotsBehaviour(CommandContext<FabricClientCommandSource> ignoredContext) {
        Notifier.send("Locked Slots Behaviour: " + ConfigManager.P_LS_BEHAVIOUR.getName(), Formatting.BLUE);
        return 1;
    }

    private static int setConfigScreen(CommandContext<FabricClientCommandSource> ignoredContext) {
        ProfilesConfigScreen.SHOULD_BE_SET = true;
        return 1;
    }
}
