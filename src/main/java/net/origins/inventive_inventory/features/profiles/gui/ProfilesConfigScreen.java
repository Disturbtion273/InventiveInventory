package net.origins.inventive_inventory.features.profiles.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.text.Text;
import net.origins.inventive_inventory.InventiveInventory;
import net.origins.inventive_inventory.features.profiles.Profile;
import net.origins.inventive_inventory.features.profiles.ProfileHandler;
import org.apache.commons.compress.archivers.sevenz.CLI;

public class ProfilesConfigScreen extends GameOptionsScreen {
    public static boolean SHOULD_BE_SET = false;
    private final DirectionalLayoutWidget layout = DirectionalLayoutWidget.vertical();

    public ProfilesConfigScreen(Screen parent) {
        super(parent, InventiveInventory.getClient().options, Text.of("Profiles Config Screen"));
        MinecraftClient client = InventiveInventory.getClient();
        this.width =  client.getWindow().getScaledWidth();
        this.height = client.getWindow().getScaledHeight();
        SHOULD_BE_SET = false;
        this.init(client, this.width, this.height);
        this.layout.forEachElement(element -> {
            ((ProfileEntry) element).forEachElement(profileElement -> {
                if (profileElement instanceof ClickableWidget) this.addDrawableChild(((ClickableWidget) profileElement));
                else if (profileElement instanceof Drawable) this.addDrawable(((Drawable) profileElement));
            });
        });
    }

    @Override
    protected void addOptions() {
        if (this.body == null) return;
        this.initBodyHeader();
        int x = this.body.getX() + 20;
        int y = this.body.getY() + 5;

        int i = 1;
        for (Profile profile : ProfileHandler.getProfiles()) {
            y += 30;
            this.layout.add(new ProfileEntry(i, x, y, profile));
            i++;
        }
    }

    private void initBodyHeader() {
        if (client == null) return;
        if (this.body == null) return;
        DirectionalLayoutWidget labelLayout = DirectionalLayoutWidget.horizontal();
        TextWidget name = new TextWidget(80, 20, Text.of("Profile Name"), this.client.textRenderer).alignCenter();
        TextWidget key = new TextWidget(20, 20, Text.of("Key"), this.client.textRenderer).alignCenter();
        TextWidget preview = new TextWidget(205, 20, Text.of("Preview"), this.client.textRenderer).alignCenter();
        labelLayout.add(name);
        labelLayout.add(key);
        labelLayout.add(EmptyWidget.ofWidth(30));
        labelLayout.add(preview);
        labelLayout.spacing(10);
        labelLayout.refreshPositions();
        labelLayout.setPosition(50 + 20, this.body.getY() + 10);
        labelLayout.forEachElement(element -> {
            if (element instanceof TextWidget) this.addDrawable((Drawable) element);
        });
    }
}
