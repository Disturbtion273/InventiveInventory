package net.origins.inventive_inventory.config.screens;

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
        GridWidget gridWidget = initializeGridWidget();
        GridWidget.Adder adder = gridWidget.createAdder(2);

        addSortingConfig(adder);
        adder.add(new EmptyWidget(1, 10), 2);
        addAutomaticRefillingConfig(adder);
        adder.add(new EmptyWidget(1, 10), 2);
        addProfilesConfig(adder);

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

    private void addSortingConfig(GridWidget.Adder adder) {
        if (client == null) return;
        adder.add(new TextWidget(Text.of("Sorting"), client.textRenderer), 2);
        adder.add(new EmptyWidget(1, 1), 2);

        adder.add(new TextWidget(Text.of("Sorting:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.SORTING.getName()).copy().setStyle(ConfigManager.SORTING.getStyle()), this.sorting()));

        adder.add(new TextWidget(Text.of("Sorting Mode:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.S_MODE.getName()), this.sortingMode()));

        adder.add(new TextWidget(Text.of("Sorting Behaviour:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.S_BEHAVIOUR.getName()), this.sortingBehaviour()));
    }

    private void addAutomaticRefillingConfig(GridWidget.Adder adder) {
        if (client == null) return;
        adder.add(new TextWidget(Text.of("Automatic Refilling"), client.textRenderer), 2);
        adder.add(new EmptyWidget(1, 1), 2);

        adder.add(new TextWidget(Text.of("Automatic Refilling:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.AUTOMATIC_REFILLING.getName()).copy().setStyle(ConfigManager.AUTOMATIC_REFILLING.getStyle()), this.automaticRefilling()));

        adder.add(new TextWidget(Text.of("Automatic Refilling Mode:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.AR_MODE.getName()), this.automaticRefillingMode()));

        adder.add(new TextWidget(Text.of("Automatic Refilling Locked Slots Behaviour:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.AR_LS_BEHAVIOUR.getName()), this.automaticRefillingLockedSlotsBehaviour()));

        adder.add(new TextWidget(Text.of("Automatic Refilling Tool Behaviour:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.AR_TOOL_BEHAVIOUR.getName()), this.automaticRefillingToolBehaviour()));
    }

    private void addProfilesConfig(GridWidget.Adder adder) {
        if (client == null) return;
        adder.add(new TextWidget(Text.of("Profiles"), client.textRenderer), 2);
        adder.add(new EmptyWidget(1, 1), 2);

        adder.add(new TextWidget(Text.of("Profiles:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.PROFILES.getName()).copy().setStyle(ConfigManager.PROFILES.getStyle()), this.profiles()));

        adder.add(new TextWidget(Text.of("Profiles Load Mode:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.P_LOAD_MODE.getName()), this.profilesLoadMode()));

        adder.add(new TextWidget(Text.of("Profiles Locked Slots Behaviour:"), client.textRenderer));
        adder.add(this.createButton(Text.of(ConfigManager.P_LS_BEHAVIOUR.getName()), this.profilesLockedSlotsBehaviour()));
    }

    private ButtonWidget.PressAction sorting() {
        return button -> {
            ConfigManager.SORTING.toggle();
            button.setMessage(Text.of(ConfigManager.SORTING.getName()).copy().setStyle(ConfigManager.SORTING.getStyle()));
            ConfigManager.save();
        };
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

    private ButtonWidget.PressAction automaticRefilling() {
        return button -> {
            ConfigManager.AUTOMATIC_REFILLING.toggle();
            button.setMessage(Text.of(ConfigManager.AUTOMATIC_REFILLING.getName()).copy().setStyle(ConfigManager.AUTOMATIC_REFILLING.getStyle()));
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

    private ButtonWidget.PressAction automaticRefillingLockedSlotsBehaviour() {
        return button -> {
            ConfigManager.AR_LS_BEHAVIOUR.toggle();
            button.setMessage(Text.of(ConfigManager.AR_LS_BEHAVIOUR.getName()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction automaticRefillingToolBehaviour() {
        return button -> {
            ConfigManager.AR_TOOL_BEHAVIOUR.toggle();
            button.setMessage(Text.of(ConfigManager.AR_TOOL_BEHAVIOUR.getName()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction profiles() {
        return button -> {
            ConfigManager.PROFILES.toggle();
            button.setMessage(Text.of(ConfigManager.PROFILES.getName()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction profilesLoadMode() {
        return button -> {
            ConfigManager.P_LOAD_MODE.toggle();
            button.setMessage(Text.of(ConfigManager.P_LOAD_MODE.getName()));
            ConfigManager.save();
        };
    }

    private ButtonWidget.PressAction profilesLockedSlotsBehaviour() {
        return button -> {
            ConfigManager.P_LS_BEHAVIOUR.toggle();
            button.setMessage(Text.of(ConfigManager.P_LS_BEHAVIOUR.getName()));
            ConfigManager.save();
        };
    }
}
