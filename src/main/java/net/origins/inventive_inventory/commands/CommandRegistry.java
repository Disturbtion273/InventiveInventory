package net.origins.inventive_inventory.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.origins.inventive_inventory.commands.automatic_refilling.AutomaticRefillingConfigCommand;
import net.origins.inventive_inventory.commands.profiles.ProfilesConfigCommand;
import net.origins.inventive_inventory.commands.profiles.ProfilesCreateCommand;
import net.origins.inventive_inventory.commands.profiles.ProfilesDeleteCommand;
import net.origins.inventive_inventory.commands.profiles.ProfilesLoadCommand;
import net.origins.inventive_inventory.commands.sorting.SortingConfigCommand;

public class CommandRegistry {

    public static void register() {
        registerSortingCommands();
        registerAutomaticRefillingCommands();
        registerProfileCommands();
    }

    private static void registerSortingCommands() {
        ClientCommandRegistrationCallback.EVENT.register(SortingConfigCommand::register);
    }

    private static void registerAutomaticRefillingCommands() {
        ClientCommandRegistrationCallback.EVENT.register(AutomaticRefillingConfigCommand::register);
    }

    private static void registerProfileCommands() {
        ClientCommandRegistrationCallback.EVENT.register(ProfilesConfigCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(ProfilesCreateCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(ProfilesLoadCommand::register);
        ClientCommandRegistrationCallback.EVENT.register(ProfilesDeleteCommand::register);
    }
}
