package net.origins.inventive_inventory.config.screens;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.config.ConfigManager;


public class ConfigScreen extends GameOptionsScreen {

    public ConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.of("Inventive Inventory Options"));
    }

    @Override
    protected void addOptions() {
        MinecraftClient client = InventiveInventory.getClient();
        GridWidget gridWidget = initializeGridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(2);

        adder.add(new TextWidget(Text.of("Sorting Mode:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.S_MODE.getName()), this.sortingMode()));

        adder.add(new TextWidget(Text.of("Sorting Behaviour:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.S_BEHAVIOUR.getName()), this.sortingBehaviour()));

        adder.add(new TextWidget(Text.of("Automatic Refilling Mode:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.AR_MODE.getName()), this.automaticRefillingMode()));

        adder.add(new TextWidget(Text.of("Automatic Refilling Behaviour:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.AR_LS_BEHAVIOUR.getName()), this.automaticRefillingBehaviour()));

        finalizeGridWidget(gridWidget);
    }

    private GridWidget initializeGridWidget() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(10).marginY(1).alignHorizontalCenter().alignVerticalCenter();
        return gridWidget;
    }

    private void finalizeGridWidget(GridWidget gridWidget) {
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 6, this.width, this.height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    private ButtonWidget createButton(Text text, ButtonWidget.PressAction pressAction) {
        return ButtonWidget.builder(text, pressAction).build();
    }


    private ButtonWidget.PressAction sortingMode() {
        return button -> {
            ConfigManager.S_MODE.toggle();
            button.setMessage(Text.of(ConfigManager.S_MODE.getName()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction sortingBehaviour() {
        return button -> {
            ConfigManager.S_BEHAVIOUR.toggle();
            button.setMessage(Text.of(ConfigManager.S_BEHAVIOUR.getName()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction automaticRefillingMode() {
        return button -> {
            ConfigManager.AR_MODE.toggle();
            button.setMessage(Text.of(ConfigManager.AR_MODE.getName()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction automaticRefillingBehaviour() {
        return button -> {
            ConfigManager.AR_LS_BEHAVIOUR.toggle();
            button.setMessage(Text.of(ConfigManager.AR_LS_BEHAVIOUR.getName()));
            ConfigManager.save();
        };
    }

}
