package net.strobel.inventive_inventory.event;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class DisconnectHandler {

    private static boolean disconnecting = false;
    public static void register() {
        ClientPlayConnectionEvents.DISCONNECT.register((server, client) -> {
            disconnecting = true;
            //PlayerSorter.confirmInventory(client.player.getInventory(), client.player.currentScreenHandler.syncId);
            disconnecting = false;
        });
    }

    public static boolean isDisconnecting() {
        return disconnecting;
    }
}
