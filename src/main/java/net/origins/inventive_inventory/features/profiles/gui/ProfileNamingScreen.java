package net.origins.inventive_inventory.features.profiles.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import org.lwjgl.glfw.GLFW;

public class ProfileNamingScreen extends Screen {
    private TextFieldWidget textFieldWidget;

    public ProfileNamingScreen() {
        super(Text.of("TextFieldScreen"));
        int width = InventiveInventory.getClient().getWindow().getScaledWidth();
        int height = InventiveInventory.getClient().getWindow().getScaledHeight();
        this.init(InventiveInventory.getClient(), width, height);
    }

    @Override
    protected void init() {
        super.init();
        if (this.client == null) return;
        int centerX = this.width / 2;
        int centerY = this.height / 2;

        DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical();

        TextWidget textWidget = new TextWidget(150, 10, Text.of("Name your profile:"), this.client.textRenderer);
        this.textFieldWidget = new TextFieldWidget(this.client.textRenderer, 150, 20, Text.empty());
        this.textFieldWidget.setPlaceholder(Text.of("Profile name..."));
        ButtonWidget doneButton = ButtonWidget.builder(ScreenTexts.DONE, (button) -> createProfile()).build();

        layout.add(textWidget);
        layout.add(this.textFieldWidget);
        layout.add(doneButton);
        layout.spacing(5);
        layout.refreshPositions();
        layout.setPosition(centerX - layout.getWidth() / 2, centerY - centerY / 2);
        layout.forEachChild(this::addDrawableChild);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);
        if (GLFW.GLFW_KEY_ENTER == keyCode) createProfile();
        return true;
    }

    private void createProfile() {
        String name = this.textFieldWidget.getText();
        if (name.isEmpty()) return;
        ProfileHandler.create(name, ProfileHandler.getAvailableProfileKey());
        this.close();
    }
}
