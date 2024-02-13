package net.strobel.inventive_inventory.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;

public class SaveProfileCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher, CommandRegistryAccess ignored) {
        dispatcher.register(ClientCommandManager.literal("inventiveprofile")
                .then(ClientCommandManager.literal("save")
                        .then(ClientCommandManager.argument("name", StringArgumentType.word())
                                .executes(SaveProfileCommand::run)
                        .then(ClientCommandManager.argument("keybind", StringArgumentType.word())
                                .executes(SaveProfileCommand::runWithKeybind)))));

    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        ProfileHandler.save(name);
        context.getSource().sendFeedback(Text.of("Saved successfully: " + name));
        return 1;
    }

    private static int runWithKeybind(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String keybind = StringArgumentType.getString(context, "keybind");
        // ProfileHandler.save(name);
        context.getSource().sendFeedback(Text.of("Saved successfully: " + name + ", " + keybind));
        return 1;
    }
}
