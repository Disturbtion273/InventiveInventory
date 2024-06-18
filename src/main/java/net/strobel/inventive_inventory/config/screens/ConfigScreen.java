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


public class ConfigScreen extends GameOptionsScreen {
    private final Screen parent;

    public ConfigScreen(Screen parent) {
        super(parent, MinecraftClient.getInstance().options, Text.of("Inventive Inventory Options"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        MinecraftClient client = InventiveInventory.getClient();
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter().alignVerticalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(new TextWidget(Text.of("Sorting Mode:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.SORTING.toString()), this.sorting()));

        adder.add(new TextWidget(Text.of("Automatic Refilling Mode:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.AUTOMATIC_REFILLING.toString()), this.automaticRefilling()));

        adder.add(new TextWidget(Text.of("Profile Fast Loading:"), client.textRenderer));
        adder.add(this.createButton(Text.of("Profiles..."), this.profileFastLoading()));
        adder.add(EmptyWidget.ofHeight(150));

        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 6 - 12, this.width, this.height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);

        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
                            ConfigManager.save();
                            client.setScreen(this.parent);
                        }).position(this.width / 2 - 100, this.height - 27)
                        .size(200, 20)
                        .build());
    }

    @Override
    public void render(DrawContext DrawContext, int mouseX, int mouseY, float delta) {
        this.renderBackground(DrawContext);
        super.render(DrawContext, mouseX, mouseY, delta);
        DrawContext.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
    }

    private ButtonWidget createButton(Text text, ButtonWidget.PressAction pressAction) {
        return ButtonWidget.builder(text, pressAction).build();
    }

    private ButtonWidget.PressAction sorting() {
        return button -> {
            if (ConfigManager.SORTING == Mode.NAME) {
                ConfigManager.SORTING = Mode.ITEM_TYPE;
            } else ConfigManager.SORTING = Mode.NAME;
            button.setMessage(Text.of(ConfigManager.SORTING.toString()));
        };
    }

    private ButtonWidget.PressAction automaticRefilling() {
        return button -> {
            if (ConfigManager.AUTOMATIC_REFILLING == Mode.STANDARD) {
                ConfigManager.AUTOMATIC_REFILLING = Mode.INVERTED;
            } else ConfigManager.AUTOMATIC_REFILLING = Mode.STANDARD;
            button.setMessage(Text.of(ConfigManager.AUTOMATIC_REFILLING.toString()));
        };
    }

    private ButtonWidget.PressAction profileFastLoading() {
        return button -> client.setScreen(new ProfileFastLoadingScreen(this.parent));
    }
}
