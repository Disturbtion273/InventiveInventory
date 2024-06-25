package net.origins.inventive_inventory.keys.handler;

import net.minecraft.client.util.InputUtil;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.keys.mixins.MixinIKeyBindingAccessor;
import org.lwjgl.glfw.GLFW;

public class AdvancedOperationHandler {
    public static AdvancedOperationMode MODE = AdvancedOperationMode.INIT;
    private static boolean pressed = false;

    public static void setPressed(boolean state) {
        pressed = state;
    }

    public static boolean isPressed() {
        return pressed;
    }

    public static boolean isReleased() {
        if (!pressed) return false;
        long window = InventiveInventory.getClient().getWindow().getHandle();
        int code = ((MixinIKeyBindingAccessor) KeyRegistry.advancedOperationKey).getBoundKey().getCode();
        if (code >= GLFW.GLFW_KEY_SPACE && code <= GLFW.GLFW_KEY_LAST) {
            return !InputUtil.isKeyPressed(window, code);
        } else if (code >= GLFW.GLFW_MOUSE_BUTTON_1 && code <= GLFW.GLFW_MOUSE_BUTTON_LAST) {
            return GLFW.glfwGetMouseButton(window, code) == GLFW.GLFW_RELEASE;
        } return true;
    }
}
