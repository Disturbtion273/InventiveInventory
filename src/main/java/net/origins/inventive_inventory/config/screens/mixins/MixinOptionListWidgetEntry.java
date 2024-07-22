package net.origins.inventive_inventory.config.screens.mixins;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.origins.inventive_inventory.config.screens.widgets.CustomTextWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(targets = "net/minecraft/client/gui/widget/OptionListWidget$WidgetEntry")
public abstract class MixinOptionListWidgetEntry {
    @Shadow @Final private Screen screen;

    @Shadow @Final private List<ClickableWidget> widgets;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void onRender(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        int i = 0;
        int j = this.screen.width / 2 - 155;
        for (ClickableWidget clickableWidget : this.widgets) {
            if (clickableWidget instanceof CustomTextWidget) clickableWidget.setPosition(j + i, y + 6);
            else clickableWidget.setPosition(j + i, y);
            clickableWidget.render(context, mouseX, mouseY, tickDelta);
            i += 160;
        }
        ci.cancel();
    }
}
