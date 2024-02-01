package net.strobel.inventive_inventory.handler;

import net.minecraft.client.util.InputUtil;
import net.strobel.inventive_inventory.InventiveInventoryClient;
import org.lwjgl.glfw.GLFW;

public class AdvancedOperationHandler {
    private static InputUtil.Key boundKey = KeyInputHandler.advancedOperationKey.getDefaultKey();
    private static boolean pressed = false;

    public static void press() {
        pressed = true;
    }

    public static void release() {
        pressed = false;
    }

    public static boolean isPressed() {
        return pressed;
    }

    public static boolean isReleased() {
        long window = InventiveInventoryClient.getClient().getWindow().getHandle();
        int code = AdvancedOperationHandler.getBoundKey().getCode();
        return GLFW.glfwGetKey(window, code) == GLFW.GLFW_RELEASE && AdvancedOperationHandler.isPressed();
    }

    public static InputUtil.Key getBoundKey() {
        return boundKey;
    }

    public static void setBoundKey(InputUtil.Key key) {
        boundKey = key;
    }
}
