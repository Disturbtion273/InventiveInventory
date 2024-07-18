package net.origins.inventive_inventory.util.mouse;

import net.minecraft.screen.slot.Slot;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.ProfilesScreen;
import net.origins.inventive_inventory.util.ScreenCheck;
import net.origins.inventive_inventory.util.slots.PlayerSlots;
import net.origins.inventive_inventory.util.slots.SlotTypes;

public class MouseLocation {
    private static Slot hoveredSlot;

    public static void setHoveredSlot(Slot slot) {
        hoveredSlot = slot;
    }

    public static Slot getHoveredSlot() {
        return hoveredSlot;
    }

    public static boolean isOverInventory() {
        if (ScreenCheck.isPlayerInventory()) return true;
        else if (hoveredSlot != null) return PlayerSlots.get().append(SlotTypes.HOTBAR).contains(hoveredSlot.id);
        else return false;
    }

    public static int getHoveredProfileSection(int mouseX, int mouseY) {
        if (ScreenCheck.isProfileScreen()) {
            ProfilesScreen screen = (ProfilesScreen) InventiveInventory.getScreen();
            int sections = screen.getSections();

            for (int section = 0; section < sections; section++) {
                int startAngle = section * (360 / sections);
                int limit = 360 / sections + startAngle;
                int x = screen.width / 2;
                int y = screen.height / 2;

                int innerRadius = ProfilesScreen.OUTER_RADIUS / 2;

                for (; startAngle < limit; startAngle++) {
                    double angle = ((startAngle * Math.PI) / 180);
                    double nextAngle = (((startAngle + 1) * Math.PI) / 180);

                    float posX = (float) (x + Math.sin(angle) * ProfilesScreen.OUTER_RADIUS);
                    float posY = (float) (y - Math.cos(angle) * ProfilesScreen.OUTER_RADIUS);

                    float posInnerX = (float) (x + Math.sin(angle) * innerRadius);
                    float posInnerY = (float) (y - Math.cos(angle) * innerRadius);

                    float nextPosX = (float) (x + Math.sin(nextAngle) * ProfilesScreen.OUTER_RADIUS);
                    float nextPosY = (float) (y - Math.cos(nextAngle) * ProfilesScreen.OUTER_RADIUS);

                    float nextPosInnerX = (float) (x + Math.sin(nextAngle) * innerRadius);
                    float nextPosInnerY = (float) (y - Math.cos(nextAngle) * innerRadius);

                    if (isPointInQuadrilateral(mouseX, mouseY, posX, posY, posInnerX, posInnerY, nextPosInnerX, nextPosInnerY, nextPosX, nextPosY)) {
                        return section;
                    }
                }
            }
        }
        return -1;
    }

    private static boolean isPointInQuadrilateral(int mouseX, int mouseY, float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        // Check if the point is in the first triangle
        boolean inFirstTriangle = isPointInTriangle(mouseX, mouseY, x1, y1, x2, y2, x3, y3);

        // Check if the point is in the second triangle
        boolean inSecondTriangle = isPointInTriangle(mouseX, mouseY, x1, y1, x3, y3, x4, y4);

        // The point is in the quadrilateral if it's in either of the triangles
        return inFirstTriangle || inSecondTriangle;
    }

    private static boolean isPointInTriangle(int mouseX, int mouseY, float x1, float y1, float x2, float y2, float x3, float y3) {
        double denominator = ((y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3));
        double a = ((y2 - y3) * (mouseX - x3) + (x3 - x2) * (mouseY - y3)) / denominator;
        double b = ((y3 - y1) * (mouseX - x3) + (x1 - x3) * (mouseY - y3)) / denominator;
        double c = 1 - a - b;

        // The point is in the triangle if 0 <= a <= 1 and 0 <= b <= 1 and 0 <= c <= 1
        return 0 <= a && a <= 1 && 0 <= b && b <= 1 && 0 <= c && c <= 1;
    }

}
