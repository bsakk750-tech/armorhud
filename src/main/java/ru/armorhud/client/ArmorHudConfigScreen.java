package ru.armorhud.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class ArmorHudConfigScreen extends Screen {
    private final Screen parent;
    private final ArmorHudConfig config;
    private ButtonWidget orientation;
    private ButtonWidget offsetX;
    private ButtonWidget offsetY;
    private ButtonWidget iconSize;
    private ButtonWidget spacing;

    public ArmorHudConfigScreen(Screen parent) {
        super(Text.translatable("screen.armorhud.title"));
        this.parent = parent;
        this.config = ArmorHudClient.CONFIG;
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int y = this.height / 2 - 70;
        orientation = addDrawableChild(ButtonWidget.builder(labelOrientation(), b -> {
            config.orientation = config.orientation == ArmorHudConfig.Orientation.HORIZONTAL
                ? ArmorHudConfig.Orientation.VERTICAL : ArmorHudConfig.Orientation.HORIZONTAL;
            b.setMessage(labelOrientation());
        }).dimensions(cx - 100, y, 200, 20).build());
        offsetX = addDrawableChild(stepButton(cx - 100, y + 26, () -> config.offsetX, v -> config.offsetX = v,
            "option.armorhud.offset_x", -200, 200));
        offsetY = addDrawableChild(stepButton(cx - 100, y + 52, () -> config.offsetY, v -> config.offsetY = v,
            "option.armorhud.offset_y", -200, 200));
        iconSize = addDrawableChild(stepButton(cx - 100, y + 78, () -> config.iconSize, v -> config.iconSize = v,
            "option.armorhud.icon_size", 8, 32));
        spacing = addDrawableChild(stepButton(cx - 100, y + 104, () -> config.spacing, v -> config.spacing = v,
            "option.armorhud.spacing", 0, 16));
        addDrawableChild(ButtonWidget.builder(Text.translatable("button.armorhud.done"), b -> close())
            .dimensions(cx - 100, y + 140, 200, 20).build());
    }

    private ButtonWidget stepButton(int x, int y, IntGetter getter, IntSetter setter, String key, int min, int max) {
        return ButtonWidget.builder(label(key, getter.get()), b -> {
            int next = getter.get() + 1;
            setter.set(next > max ? min : next);
            b.setMessage(label(key, getter.get()));
        }).dimensions(x, y, 200, 20).build();
    }

    private Text label(String key, int value) { return Text.translatable(key, value); }
    private Text labelOrientation() {
        return Text.translatable("option.armorhud.orientation").copy().append(": ")
            .append(Text.translatable(config.orientation == ArmorHudConfig.Orientation.HORIZONTAL
                ? "option.armorhud.orientation.horizontal" : "option.armorhud.orientation.vertical"));
    }

    @Override
    public void close() {
        config.save();
        this.client.setScreen(parent);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 35, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @FunctionalInterface private interface IntGetter { int get(); }
    @FunctionalInterface private interface IntSetter { void set(int value); }
}
