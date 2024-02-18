package net.strobel.inventive_inventory.keybindfix.mixins;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface MixinIKeyBindingAccessor {

    @Accessor(value = "timesPressed")
    int getTimesPressed();

    @Accessor(value = "timesPressed")
    void setTimesPressed(int value);

    @Accessor(value = "boundKey")
    InputUtil.Key getBoundKey();
}
