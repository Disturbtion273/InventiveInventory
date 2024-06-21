package net.origins.inventive_inventory.keys.mixins;

import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(KeyBinding.class)
public interface MixinIKeyBindingAccessor {

    @Accessor(value = "boundKey")
    InputUtil.Key getBoundKey();
}
