package net.origins.inventive_inventory.features.profiles;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.keys.KeyRegistry;

public class ProfilesScreen extends Screen {

    public ProfilesScreen() {
        super(Text.literal("Profile Screen"));
        this.client.setOverlay(null);

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.client.world == null) {
            this.renderInGameBackground(context);
        }
        this.renderDarkening(context);
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
            this.close();
            return true;
        } return false;
    }
}
