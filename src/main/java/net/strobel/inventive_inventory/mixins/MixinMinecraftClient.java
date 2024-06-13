package net.strobel.inventive_inventory.mixins;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.world.ClientWorld;
import net.strobel.inventive_inventory.features.profiles.ProfileHandler;
import net.strobel.inventive_inventory.handler.KeyInputHandler;
import net.strobel.inventive_inventory.keybindfix.MixinIKeyBindingDisplay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Mixin(MinecraftClient.class)
public class MixinMinecraftClient {

    @Inject(method = "joinWorld", at = @At("HEAD"))
    private void onJoinWorld(ClientWorld world, DownloadingTerrainScreen.WorldEntryReason worldEntryReason, CallbackInfo ci) {
        ProfileHandler.initialize();
    }

    @Inject(method = "stop", at = @At("HEAD"))
    private void onStop(CallbackInfo ci) {
        List<String> newEntries = new ArrayList<>();

        String filePath = FabricLoader.getInstance().getGameDir().resolve("options.txt").toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                for (KeyBinding keyBinding : KeyInputHandler.profileKeys) {
                    String displayName = ((MixinIKeyBindingDisplay) keyBinding).main$getDisplayName();
                    if (line.startsWith("key_Profile: " + displayName)) {
                        ((MixinIKeyBindingDisplay) keyBinding).main$resetDisplayName();
                        line = line.replaceFirst("Profile: " + displayName, keyBinding.getTranslationKey());
                        break;
                    }
                }
                newEntries.add(line);
            }
        } catch (IOException ignored) {
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String line : newEntries) {
                writer.write(line);
                writer.newLine(); // This will write a new line separator
            }
        } catch (IOException ignored) {}
    }
}
