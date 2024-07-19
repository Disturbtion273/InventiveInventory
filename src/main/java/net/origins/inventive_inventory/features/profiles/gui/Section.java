package net.origins.inventive_inventory.features.profiles.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.BufferBuilder;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.util.Textures;
import net.origins.inventive_inventory.util.mouse.MouseLocation;

public class Section {
    private final int ID;
    private final Profile profile;

    public Section(int ID, Profile profile) {
        this.ID = ID;
        this.profile = profile;
    }

    public Profile getProfile() {
        return profile;
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return MouseLocation.getHoveredProfileSection(mouseX, mouseY) == this.ID;
    }

    public void drawBackground(BufferBuilder builder, boolean hovered) {
        int startAngle = 360 / ProfilesScreen.getSections().size() * this.ID;
        int limit = 360 / ProfilesScreen.getSections().size() + startAngle;
        int centerX = InventiveInventory.getScreen().width / 2;
        int centerY = InventiveInventory.getScreen().height / 2;

        int innerRadius = ProfilesScreen.RADIUS / 2;

        for (; startAngle < limit; startAngle++) {
            double angle = (startAngle * Math.PI) / 180;
            double nextAngle = ((startAngle + 1) * Math.PI) / 180;

            float posX = centerX + (float) Math.sin(angle) * ProfilesScreen.RADIUS;
            float posY = centerY - (float) Math.cos(angle) * ProfilesScreen.RADIUS;

            float posInnerX = centerX + (float) Math.sin(angle) * innerRadius;
            float posInnerY = centerY - (float) Math.cos(angle) * innerRadius;

            float nextPosX = centerX + (float) Math.sin(nextAngle) * ProfilesScreen.RADIUS;
            float nextPosY = centerY - (float) Math.cos(nextAngle) * ProfilesScreen.RADIUS;

            float nextPosInnerX = centerX + (float) Math.sin(nextAngle) * innerRadius;
            float nextPosInnerY = centerY - (float) Math.cos(nextAngle) * innerRadius;

            int color = hovered ? ProfilesScreen.HOVER_COLOR : ProfilesScreen.COLOR;
            builder.vertex(posX, posY, 0).color(color)
                    .vertex(posInnerX, posInnerY, 0).color(color)
                    .vertex(nextPosInnerX, nextPosInnerY, 0).color(color)
                    .vertex(nextPosX, nextPosY, 0).color(color);
        }
    }

    public void drawIcon(DrawContext context) {
        int startAngle = 360 / ProfilesScreen.getSections().size() * this.ID;
        int limit = 360 / ProfilesScreen.getSections().size() + startAngle;
        int centerX = InventiveInventory.getScreen().width / 2;
        int centerY = InventiveInventory.getScreen().height / 2;

        int innerRadius = ProfilesScreen.RADIUS / 2;

        int middleAngle = startAngle + (limit - startAngle) / 2;
        double middleRadian = (middleAngle * Math.PI) / 180;

        int middleX = (int) (centerX + Math.sin(middleRadian) * (ProfilesScreen.RADIUS - ((double) (ProfilesScreen.RADIUS - innerRadius) / 2)));
        int middleY = (int) (centerY - Math.cos(middleRadian) * (ProfilesScreen.RADIUS - ((double) (ProfilesScreen.RADIUS - innerRadius) / 2)));

        if (this.profile == null) {
            context.drawTexture(Textures.PLUS, middleX - 8, middleY - 8, 0, 0, 0, 16, 16, 16, 16);
        } else if (this.profile.getDisplayStack() == null) {
            context.drawTexture(Textures.TOOLS, middleX - 8, middleY - 8, 0, 0, 0, 16, 16, 16, 16);
        } else context.drawItem(this.profile.getDisplayStack(), middleX - 8, middleY - 8);
    }
}