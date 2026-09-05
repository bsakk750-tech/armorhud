package ru.armorhud.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

public final class ArmorHudConfig {
    public enum Orientation { HORIZONTAL, VERTICAL }

    public Orientation orientation = Orientation.HORIZONTAL;
    public int offsetX = 4;
    public int offsetY = 0;
    public int iconSize = 16;
    public int spacing = 2;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path FILE = FabricLoader.getInstance().getConfigDir().resolve("armorhud.json");

    public static ArmorHudConfig load() {
        try {
            if (Files.exists(FILE)) {
                ArmorHudConfig config = GSON.fromJson(Files.readString(FILE), ArmorHudConfig.class);
                if (config != null) {
                    config.clamp();
                    return config;
                }
            }
        } catch (Exception ignored) { }
        return new ArmorHudConfig();
    }

    public void save() {
        clamp();
        try {
            Files.createDirectories(FILE.getParent());
            Files.writeString(FILE, GSON.toJson(this));
        } catch (IOException ignored) { }
    }

    public void clamp() {
        if (orientation == null) orientation = Orientation.HORIZONTAL;
        offsetX = Math.max(-200, Math.min(200, offsetX));
        offsetY = Math.max(-200, Math.min(200, offsetY));
        iconSize = Math.max(8, Math.min(32, iconSize));
        spacing = Math.max(0, Math.min(16, spacing));
    }
}
