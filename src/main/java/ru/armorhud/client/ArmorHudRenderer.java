package ru.armorhud.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

public final class ArmorHudRenderer {
    private static final EquipmentSlot[] SLOTS = {
        EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET
    };
    private static final ItemStack[] PLACEHOLDERS = {
        new ItemStack(Items.LEATHER_HELMET), new ItemStack(Items.LEATHER_CHESTPLATE),
        new ItemStack(Items.LEATHER_LEGGINGS), new ItemStack(Items.LEATHER_BOOTS)
    };

    private ArmorHudRenderer() { }

    public static void render(DrawContext context, net.minecraft.client.render.RenderTickCounter tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;
        ArmorHudConfig config = ArmorHudClient.CONFIG;
        if (player == null || config == null || client.options.hudHidden) return;

        int size = config.iconSize;
        int gap = config.spacing;
        int baseX = client.getWindow().getScaledWidth() / 2 + 91 + config.offsetX;
        int baseY = client.getWindow().getScaledHeight() - size - 4 + config.offsetY;

        for (int i = 0; i < SLOTS.length; i++) {
            int x = baseX + (config.orientation == ArmorHudConfig.Orientation.HORIZONTAL ? i * (size + gap) : 0);
            int y = baseY - (config.orientation == ArmorHudConfig.Orientation.VERTICAL ? i * (size + gap) : 0);
            ItemStack stack = player.getEquippedStack(SLOTS[i]);
            boolean equipped = !stack.isEmpty();

            context.fill(x - 1, y - 1, x + size + 1, y + size + 1, 0x90000000);
            if (equipped) {
                drawScaledItem(context, stack, x, y, size);
                int percent = durabilityPercent(stack);
                int color = percent > 50 ? 0xFFFFFFFF : percent > 20 ? 0xFFFFC107 : 0xFFFF5555;
                String label = percent + "%";
                context.drawTextWithShadow(client.textRenderer, Text.literal(label), x + 1, y + size - 9, color);
            } else {
                drawScaledItem(context, PLACEHOLDERS[i], x, y, size);
                context.fill(x, y, x + size, y + size, 0x70000000);
                context.drawBorder(x, y, size, size, 0xB0B0B0B0);
            }
        }
    }

    private static int durabilityPercent(ItemStack stack) {
        int max = stack.getMaxDamage();
        if (max <= 0) return 100;
        return Math.max(0, Math.min(100, Math.round((max - stack.getDamage()) * 100.0f / max)));
    }

    private static void drawScaledItem(DrawContext context, ItemStack stack, int x, int y, int size) {
        context.getMatrices().push();
        float scale = size / 16.0f;
        context.getMatrices().translate(x, y, 0);
        context.getMatrices().scale(scale, scale, 1.0f);
        context.drawItem(stack, 0, 0);
        context.getMatrices().pop();
    }
}
