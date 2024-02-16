package net.strobel.inventive_inventory.keybindfix.mixins;

import net.minecraft.client.gui.screen.option.ControlsListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(ControlsListWidget.class)
public class MixinControlsListWidget {

    @Redirect(method = "<init>",
            at = @At(value = "INVOKE", target = "Ljava/util/Arrays;sort([Ljava/lang/Object;)V")
    )
    private void sortKeyBindings(Object[] a) {}

}
