package net.origins.inventive_inventory.features.profiles.gui;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.gui.widget.WrapperWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.origins.inventive_inventory.features.profiles.SavedSlot;
import net.origins.inventive_inventory.util.Textures;

import java.util.List;
import java.util.function.Consumer;

public class Hotbar extends WrapperWidget implements Drawable {
    private List<SavedSlot> savedSlots;

    public Hotbar(int x, int y, List<SavedSlot> savedSlots) {
        super(x, y, 205, 20);
        this.savedSlots = savedSlots;
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(Textures.HOTBAR, this.getX(), this.getY(), 0, 0, 0, 205, 20, 205, 20);
        context.fill(this.getX() + 2, this.getY() + 2, this.getX() + 2 + 16, this.getY() + 2 + 16, -2130706433);
        int x = this.getX() + 27;
        int y = this.getY() + 2;
        for (int i = 0; i < 9; i++) {
            context.fill(x, y, x + 16, y + 16, -2130706433);
            x += 20;
        }
        for (int i = 0; i < 9; i++) {
            
        }

        for (SavedSlot savedSlot : this.savedSlots) {
            if (savedSlot.slot() == PlayerScreenHandler.OFFHAND_ID) context.drawItem(savedSlot.stack(), this.getX() + 2, this.getY() + 2);

        }
    }

    @Override
    public void forEachElement(Consumer<Widget> consumer) {

    }
}
