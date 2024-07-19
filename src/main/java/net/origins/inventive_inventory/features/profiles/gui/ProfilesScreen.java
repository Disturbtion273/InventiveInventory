package net.origins.inventive_inventory.features.profiles.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.*;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.util.mouse.MouseLocation;

import java.util.ArrayList;
import java.util.List;

public class ProfilesScreen extends Screen {
    public static int RADIUS = 60;
    public static final int COLOR = 0x7F000000;
    public static final int HOVER_COLOR = 0x3FFFFFFF;
    private static final int MAX_PROFILES = 6;
    private static final List<Section> sections = new ArrayList<>();

    private int mouseX;
    private int mouseY;

    public ProfilesScreen() {
        super(Text.literal("Profile Screen"));
        if (this.client != null) this.client.setOverlay(null);
        sections.clear();
        for (Profile profile: ProfileHandler.getProfiles()) {
            if (sections.size() <= MAX_PROFILES) sections.add(new Section(sections.size(), profile));
        }
        if (sections.size() < MAX_PROFILES) sections.add(new Section(sections.size(), null));
    }

    public static List<Section> getSections() {
        return sections;
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
        RenderSystem.setShader(GameRenderer::getPositionColorProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        BufferBuilder builder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);

        for (Section section : sections) {
            section.drawBackground(builder, section.isHovered(mouseX, mouseY));
        }
        BufferRenderer.drawWithGlobalProgram(builder.end());

        for (Section section : sections) {
            section.drawIcon(context);
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
            int section = MouseLocation.getHoveredProfileSection(this.mouseX, this.mouseY);
            if (section != -1) {
                if (sections.get(section).getProfile() == null) {
                    ProfileHandler.create("");
                } else {
                    // TODO: LOAD OR OVERWRITE
                    ProfileHandler.load(ProfileHandler.getProfiles().get(section));
                }
            }
            this.close();
            return true;
        } return false;
    }
}
