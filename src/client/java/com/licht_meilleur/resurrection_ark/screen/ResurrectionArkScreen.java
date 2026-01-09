package com.licht_meilleur.resurrection_ark.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

/**
 * Resurrection Ark のGUI画面
 */
public class ResurrectionArkScreen extends HandledScreen<ResurrectionArkScreenHandler> {
    private static final Identifier TEXTURE =
            new Identifier("resurrection_ark", "textures/gui/resurrection_ark_gui.png");

    // スクロール位置
    private int scrollOffset = 0;

    // 1ページに表示するmob数（縦に2体）
    private static final int MOBS_PER_PAGE = 2;

    public ResurrectionArkScreen(ResurrectionArkScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 176; // GUI幅
        this.backgroundHeight = 240; // GUI高さ（縦長）
    }

    @Override
    protected void init() {
        super.init();
        // ボタン例: 蘇生ボタン
        this.addDrawableChild(ButtonWidget.builder(Text.literal("蘇生"), b -> {
            handler.resurrectMob(scrollOffset); // 今の選択中のmobを蘇生
        }).dimensions(this.x + 120, this.y + 200, 40, 20).build());
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        // マウスホイールでスクロール
        int mobCount = handler.getMobList().size();
        int maxOffset = Math.max(0, mobCount - MOBS_PER_PAGE);
        scrollOffset = (int) Math.max(0, Math.min(scrollOffset - amount, maxOffset));
        return true;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE);
        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        List<ResurrectionArkScreenHandler.MobEntry> mobs = handler.getMobList();

        // 表示するmob（2体ぶん）
        for (int i = 0; i < MOBS_PER_PAGE; i++) {
            int index = scrollOffset + i;
            if (index >= mobs.size()) break;

            ResurrectionArkScreenHandler.MobEntry entry = mobs.get(index);

            // 名前を描画
            context.drawText(this.textRenderer, entry.name, x + 10, y + 20 + i * 100, 0xFFFFFF, false);

            // モブモデルを描画（簡易版: EntityTypeから生成）
            LivingEntity renderEntity = entry.getEntity(this.client.world);
            if (renderEntity != null) {
                int drawX = x + 40;
                int drawY = y + 60 + i * 100;
                int scale = 25; // 固定スケール
                drawEntity(context, drawX, drawY, scale, mouseX - drawX, mouseY - drawY, renderEntity);
            }
        }
    }

    // エンティティをGUIに描画するヘルパー
    private void drawEntity(DrawContext context, int x, int y, int size, float mouseX, float mouseY, LivingEntity entity) {
        context.drawEntity(x, y, size, mouseX, mouseY, entity);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // タイトルは非表示でもOK
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}