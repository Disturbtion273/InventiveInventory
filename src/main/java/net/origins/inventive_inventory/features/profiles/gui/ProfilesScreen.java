package net.origins.inventive_inventory.features.profiles.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import net.origins.inventive_inventory.keys.KeyRegistry;
import net.origins.inventive_inventory.util.mouse.MouseLocation;

import java.util.ArrayList;
import java.util.List;

public class ProfilesScreen extends HandledScreen<ScreenHandler> {
    public static final int RADIUS = 60;
    public static final int COLOR = 0x7F000000;
    public static final int HOVER_COLOR = 0x3FFFFFFF;
    public static final int DELETE_COLOR = 0x7FE4080A;
    public static final int OVERWRITE_COLOR = 0x7FFFDE59;
    private static final int MAX_PROFILES = 6;
    public static boolean DELETE_KEY_PRESSED;
    public static boolean OVERWRITE_KEY_PRESSED;
    private static final List<Section> sections = new ArrayList<>();

    private int mouseX;
    private int mouseY;

    public ProfilesScreen() {
        super(InventiveInventory.getScreenHandler(), InventiveInventory.getPlayer().getInventory(), Text.literal("Profile Screen"));
        if (this.client != null) this.client.setOverlay(null);
        sections.clear();
        for (Profile profile: ProfileHandler.getProfiles()) {
            if (sections.size() <= MAX_PROFILES) sections.add(new Section(sections.size(), profile));
        }
        if (sections.size() < MAX_PROFILES) sections.add(new Section(sections.size(), null));
        DELETE_KEY_PRESSED = false;
        OVERWRITE_KEY_PRESSED = false;
    }

    public static List<Section> getSections() {
        return sections;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
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

        for (Section section : sections) {
            section.drawTooltips(context, mouseX, mouseY);
        }

        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (InputUtil.GLFW_KEY_LEFT_ALT == keyCode) {
            DELETE_KEY_PRESSED = true;
            return true;
        } else if (InputUtil.GLFW_KEY_LEFT_CONTROL == keyCode) {
            OVERWRITE_KEY_PRESSED = true;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (KeyRegistry.openProfilesScreenKey.matchesKey(keyCode, scanCode)) {
            int section = MouseLocation.getHoveredProfileSection(this.mouseX, this.mouseY);
            if (section != -1) {
                Profile profile = sections.get(section).getProfile();
                if (profile == null) {
                    if (DELETE_KEY_PRESSED) {
                        InventiveInventory.getClient().setScreen(new ProfileNamingScreen());
                        return true;
                    } else {
                        ProfileHandler.create("", ProfileHandler.getAvailableProfileKey());
                    }
                } else if (DELETE_KEY_PRESSED) {
                    ProfileHandler.delete(profile);
                } else if (OVERWRITE_KEY_PRESSED) {
                    ProfileHandler.overwrite(profile);
                } else {
                    ProfileHandler.load(profile);
                }
            }
            this.close();
            return true;
        }
        if (InputUtil.GLFW_KEY_LEFT_CONTROL == keyCode) {
            OVERWRITE_KEY_PRESSED = false;
            return true;
        }
        if (InputUtil.GLFW_KEY_LEFT_ALT == keyCode) {
            DELETE_KEY_PRESSED = false;
            return true;
        }
        return false;
    }
}
