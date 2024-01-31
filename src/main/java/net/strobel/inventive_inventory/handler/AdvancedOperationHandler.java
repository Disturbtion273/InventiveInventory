package net.strobel.inventive_inventory.handler;

import net.minecraft.client.util.InputUtil;
import net.strobel.inventive_inventory.InventiveInventoryClient;

import org.lwjgl.glfw.GLFW;

public class AdvancedOperationHandler {
    private static InputUtil.Key boundKey = KeyInputHandler.advancedOperationKey.getDefaultKey();
    private static boolean pressed = false;

    public static void press() {
        pressed = true;
        System.out.println("Pressed");
    }

    public static void release() {
        pressed = false;
        System.out.println("Released");
    }

    public static boolean isPressed() {
        return pressed;
    }

    public static boolean isReleased() {
        long window = InventiveInventoryClient.getClient().getWindow().getHandle();
        int keyState = GLFW.glfwGetKey(window, boundKey.getCode());
        System.out.println(keyState);

        return keyState == GLFW.GLFW_RELEASE && pressed;
    }

    public static void setBoundKey(InputUtil.Key key) {
        boundKey = key;
    }
}
