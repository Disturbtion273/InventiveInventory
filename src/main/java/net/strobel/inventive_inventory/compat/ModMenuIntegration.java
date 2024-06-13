package net.strobel.inventive_inventory.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.strobel.inventive_inventory.config.screens.ConfigScreen;

public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ConfigScreen::new;
    }

}
