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
                                .executes(SaveProfileCommand::run))));

    }

    private static int run(CommandContext<FabricClientCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        ProfileHandler.save(name);
        context.getSource().sendFeedback(Text.of("Saved successfully: " + name));
        return 1;
    }
}
