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
        adder.add(this.createButton(Text.of(ConfigManager.SORTING_MODE.toString()), this.sortingMode()));

        adder.add(new TextWidget(Text.of("Sorting Behaviour:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.SORTING_BEHAVIOUR.toString()), this.sortingBehaviour()));

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
            ConfigManager.SORTING_MODE.toggle();
            button.setMessage(Text.of(ConfigManager.SORTING_MODE.toString()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction sortingBehaviour() {
        return button -> {
            ConfigManager.SORTING_BEHAVIOUR.toggle();
            button.setMessage(Text.of(ConfigManager.SORTING_BEHAVIOUR.toString()));
            ConfigManager.save();
        };
    }

}
