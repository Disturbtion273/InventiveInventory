package net.origins.inventive_inventory.features.profiles.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.*;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.keys.KeyRegistry;

public class ProfileEntry extends DirectionalLayoutWidget implements Drawable {

    public ProfileEntry(int index, int x, int y, Profile profile) {
        super(x, y, DisplayAxis.HORIZONTAL);
        int height = 20;
        MinecraftClient client = InventiveInventory.getClient();
        TextWidget indexText = new TextWidget(40, height, Text.of(index + "."), client.textRenderer);
        TextFieldWidget nameTextField = new TextFieldWidget(client.textRenderer, 80, height, Text.empty());
        nameTextField.setText(profile.getName());
        nameTextField.setPlaceholder(Text.of("Name..."));
        TextFieldWidget keyTextField = new TextFieldWidget(client.textRenderer, 20, height, Text.empty());
        KeyBinding keyBinding = KeyRegistry.getByTranslationKey(profile.getKey());
        if (keyBinding != null) keyTextField.setText(keyBinding.getBoundKeyLocalizedText().getString());
        Hotbar hotbar = new Hotbar(210, y, profile.getSavedSlots());

        this.add(indexText);
        this.add(nameTextField);
        this.add(keyTextField);
        this.add(EmptyWidget.ofWidth(30));
        this.add(hotbar);
        this.spacing(10);
        this.refreshPositions();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.forEachElement(element -> {
            if (element instanceof ClickableWidget) ((ClickableWidget) element).render(context, mouseX, mouseY, delta);
        });
    }
}
