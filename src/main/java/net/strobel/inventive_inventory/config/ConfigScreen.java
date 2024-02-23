package net.strobel.inventive_inventory.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.GridWidget;
import net.minecraft.client.gui.widget.SimplePositioningWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen {

    private final ButtonWidget REFILLING = this.createButton(Text.of(ConfigManager.AUTOMATIC_REFILLING.toString()), this.automaticRefilling());

    public ConfigScreen(Text text) {
        super(text);
    }

    @Override
    protected void init() {
        GridWidget gridWidget = new GridWidget();
        gridWidget.getMainPositioner().marginX(5).marginBottom(4).alignHorizontalCenter().alignVerticalCenter();
        GridWidget.Adder adder = gridWidget.createAdder(2);
        adder.add(new TextWidget(Text.of("Automatic Refilling Mode:"), client.textRenderer));
        adder.add(this.REFILLING);
        gridWidget.refreshPositions();
        SimplePositioningWidget.setPos(gridWidget, 0, this.height / 6 - 12, this.width, this.height, 0.5f, 0.0f);
        gridWidget.forEachChild(this::addDrawableChild);
    }

    private ButtonWidget createButton(Text text, ButtonWidget.PressAction pressAction) {
        return ButtonWidget.builder(text, pressAction).build();
    }

    private ButtonWidget.PressAction automaticRefilling() {
        return button -> {
            if (ConfigManager.AUTOMATIC_REFILLING == Mode.STANDARD) {
                ConfigManager.AUTOMATIC_REFILLING = Mode.INVERTED;
            } else ConfigManager.AUTOMATIC_REFILLING = Mode.STANDARD;
            this.REFILLING.setMessage(Text.of(ConfigManager.AUTOMATIC_REFILLING.toString()));
        };
    }
}
