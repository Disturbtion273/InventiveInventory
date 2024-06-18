package net.strobel.inventive_inventory.config.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.strobel.inventive_inventory.InventiveInventory;
import net.strobel.inventive_inventory.config.ConfigManager;
import net.strobel.inventive_inventory.config.Mode;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.handler.KeyInputHandler;


public class ProfileFastLoadingScreen extends GameOptionsScreen {
    private final Screen parent;

    public ProfileFastLoadingScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("Profile Fast Loading Options"));
        this.parent = parent;
        ProfileHandler.initialize();
    }

    @Override
    protected void init() {
        MinecraftClient client = InventiveInventory.getClient();
        OptionListWidget optionListWidget = this.addDrawableChild(new OptionListWidget(this.client, this.width, this));

        for (int i = 0; i < KeyInputHandler.profileKeys.length; i++) {
            TextWidget textWidget = new TextWidget(Text.of("Profile: " + ProfileHandler.profileNames.get(i)), client.textRenderer);
            textWidget.setHeight(20);
            ButtonWidget button = this.createButton(Text.of(ConfigManager.PROFILE_FAST_LOADING.get(i).toString()), this.changeState(i));
            optionListWidget.addWidgetEntry(textWidget, button);
        }

        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
                            ConfigManager.save();
                            client.setScreen(this.parent);
                        }).position(this.width / 2 - 100, this.height - 27)
                        .size(200, 20)
                        .build());
    }

    @Override
    protected void addOptions() {

    }

    @Override
    public void render(DrawContext DrawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(DrawContext, mouseX, mouseY, delta);
        super.render(DrawContext, mouseX, mouseY, delta);
        DrawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
    }

    private ButtonWidget createButton(Text text, ButtonWidget.PressAction pressAction) {
        return ButtonWidget.builder(text, pressAction).build();
    }

    private ButtonWidget.PressAction changeState(int i) {
        return button -> {
            if (ConfigManager.PROFILE_FAST_LOADING.get(i) == Mode.STANDARD) {
                ConfigManager.PROFILE_FAST_LOADING.set(i, Mode.FAST_LOAD);
            } else ConfigManager.PROFILE_FAST_LOADING.set(i, Mode.STANDARD);
            button.setMessage(Text.of(ConfigManager.PROFILE_FAST_LOADING.get(i).toString()));
        };
    }
}
