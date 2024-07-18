package net.origins.inventive_inventory.features.profiles;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.util.mouse.MouseLocation;

public class ProfilesScreen extends Screen {
    public static int OUTER_RADIUS = 60;
    private static final int COLOR = 0x7F000000;
    private static final int HOVER_COLOR = 0x3FFFFFFF;
    private final int sections;
    private static final Identifier PLUS_TEXTURE = Identifier.of(InventiveInventory.MOD_ID, "textures/gui/plus.png");
    private int mouseX;
    private int mouseY;

    public ProfilesScreen(int sections) {
        super(Text.literal("Profile Screen"));
        if (this.client != null) this.client.setOverlay(null);
        this.sections = sections;
    }

    public int getSections() {
        return this.sections;
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client != null && this.client.world == null) {
            this.renderInGameBackground(context);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.mouseX = mouseX;
        this.mouseY = mouseY;

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        for (int i = 0; i < this.sections; i++) {
            int color = MouseLocation.getHoveredProfileSection(mouseX, mouseY) == i ? HOVER_COLOR : COLOR;
            this.drawSection(builder, context, i * (360 / this.sections), color);
        }
        RenderSystem.disableBlend();
    }

    @Override
    public boolean shouldPause() { return false; }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (KeyRegistry.openProfilesScreenKey.matchesKey(keyCode, scanCode)) {
            if (MouseLocation.getHoveredProfileSection(this.mouseX, this.mouseY) == 0) {
                ProfileHandler.create("");
            } else if (MouseLocation.getHoveredProfileSection(this.mouseX, this.mouseY) > 0) {
                // TODO: LADEN ODER OVERWRITEN
            }
            this.close();
            return true;
        } return false;
    }

    private void drawSection(BufferBuilder builder, DrawContext context, int startAngle, int color) {
        int limit = 360 / this.sections + startAngle;
        int x = this.width / 2;
        int y = this.height / 2;

        int innerRadius = OUTER_RADIUS / 2;

        int middleAngle = startAngle + (limit - startAngle) / 2;
        double middleRadian = (middleAngle * Math.PI) / 180;

        int middleX = (int) (x + Math.sin(middleRadian) * (OUTER_RADIUS - ((double) (OUTER_RADIUS - innerRadius) / 2)));
        int middleY = (int) (y - Math.cos(middleRadian) * (OUTER_RADIUS - ((double) (OUTER_RADIUS - innerRadius) / 2)));

        for (; startAngle < limit; startAngle++) {
            double angle = (startAngle * Math.PI) / 180;
            double nextAngle = ((startAngle + 1) * Math.PI) / 180;

            float posX = (float) (x + Math.sin(angle) * OUTER_RADIUS);
            float posY = (float) (y - Math.cos(angle) * OUTER_RADIUS);

            float posInnerX = (float) (x + Math.sin(angle) * innerRadius);
            float posInnerY = (float) (y - Math.cos(angle) * innerRadius);

            float nextPosX = (float) (x + Math.sin(nextAngle) * OUTER_RADIUS);
            float nextPosY = (float) (y - Math.cos(nextAngle) * OUTER_RADIUS);

            float nextPosInnerX = (float) (x + Math.sin(nextAngle) * innerRadius);
            float nextPosInnerY = (float) (y - Math.cos(nextAngle) * innerRadius);

            builder.vertex(posX, posY, 0).color(color);
            builder.vertex(posInnerX, posInnerY, 0).color(color);
            builder.vertex(nextPosInnerX, nextPosInnerY, 0).color(color);
            builder.vertex(nextPosX, nextPosY, 0).color(color);
        }
        BufferRenderer.drawWithGlobalProgram(builder.end());

        boolean hasProfile = false;
        if (hasProfile) {
            context.drawItem(Items.DIAMOND_PICKAXE.getDefaultStack(), middleX - 8, middleY - 8);
        } else context.drawTexture(PLUS_TEXTURE, middleX - 8, middleY - 8, 0, 0, 0, 16, 16, 16, 16);

    }
}
