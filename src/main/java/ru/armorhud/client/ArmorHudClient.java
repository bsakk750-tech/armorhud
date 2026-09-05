package ru.armorhud.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

public final class ArmorHudClient implements ClientModInitializer {
    public static ArmorHudConfig CONFIG;

    @Override
    public void onInitializeClient() {
        CONFIG = ArmorHudConfig.load();
        HudRenderCallback.EVENT.register(ArmorHudRenderer::render);
    }
}
