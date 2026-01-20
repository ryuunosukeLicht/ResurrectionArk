package com.licht_meilleur.resurrection_ark.screen;

import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import com.licht_meilleur.resurrection_ark.network.ResurrectionArkServerPackets;
import net.fabricmc.fabric.api.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ConfirmScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ResurrectionArkScreen extends HandledScreen<ResurrectionArkScreenHandler> {

    private CardListWidget cardList;

    public ResurrectionArkScreen(ResurrectionArkScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundWidth = 255;
        this.backgroundHeight = 156;
    }

    @Override
    protected void init() {
        super.init();

        int left = this.x;
        int top  = this.y;

        // カード一覧領域（画面に合わせて調整OK）
        int listX = left;
        int listY = top;
        int listW = 255;
        int listH = 156 + 40;

        // ★削除クリック時のコールバックを渡す
        cardList = new CardListWidget(listX, listY, listW, listH, this::requestDelete);

        // ★開いているArkのBlockEntityから読む
        if (this.client != null && this.client.world != null) {
            BlockEntity be = this.client.world.getBlockEntity(handler.getArkPos());
            if (be instanceof ResurrectionArkBlockEntity arkBe) {
                for (ResurrectionArkBlockEntity.StoredMob mob : arkBe.getStoredMobs()) {

                    Identifier id = mob.typeId;
                    EntityType<?> type = Registries.ENTITY_TYPE.get(id);

                    var created = type.create(this.client.world);
                    if (created instanceof LivingEntity) {
                        @SuppressWarnings("unchecked")
                        EntityType<? extends LivingEntity> livingType = (EntityType<? extends LivingEntity>) type;

                        cardList.addCard(new CardListWidget.CardData(
                                mob.name,
                                livingType,
                                32,
                                28
                        ));
                    }
                }
            }
        }

        this.addDrawableChild(cardList);
        this.addSelectableChild(cardList);
    }

    // ✅ Screen側は背景を描かない（カード側が描くので二重回避）
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // 何もしない
    }

    // ✅ ホイールを CardListWidget に渡す
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (cardList != null && cardList.isMouseOver(mouseX, mouseY)) {
            return cardList.mouseScrolled(mouseX, mouseY, amount);
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        // タイトル不要
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    // ==========================
    // 削除フロー
    // ==========================

    private void requestDelete(int index) {
        MinecraftClient client = this.client;
        if (client == null) return;

        client.setScreen(new ConfirmScreen(result -> {
            if (result) {
                // 見た目を即反映（サーバ同期が遅くてもOK）
                if (cardList != null) {
                    cardList.removeCard(index);
                }
                sendDeletePacket(index);
            }
            client.setScreen(this); // 戻る
        }, Text.literal("削除確認"), Text.literal("この登録を削除しますか？")));
    }

    private void sendDeletePacket(int index) {
        if (this.client == null || this.client.player == null) return;

        var buf = PacketByteBufs.create();
        buf.writeBlockPos(handler.getArkPos());
        buf.writeInt(index);

        ClientPlayNetworking.send(ResurrectionArkServerPackets.DELETE_MOB, buf);
    }
}