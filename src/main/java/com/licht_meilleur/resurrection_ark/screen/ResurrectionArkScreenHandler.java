package com.licht_meilleur.resurrection_ark.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class ResurrectionArkScreenHandler extends ScreenHandler {

    private final BlockPos arkPos;

    // サーバ側：BlockEntity#createMenu から呼ばれる（arkPosはBlockEntityのpos）
    public ResurrectionArkScreenHandler(int syncId, PlayerInventory inventory, BlockPos arkPos) {
        super(ModScreenHandlers.RESURRECTION_ARK, syncId);
        this.arkPos = arkPos;
    }

    // クライアント側：ExtendedScreenHandlerType から呼ばれる（bufからposを受け取る）
    public ResurrectionArkScreenHandler(int syncId, PlayerInventory inventory, PacketByteBuf buf) {
        this(syncId, inventory, buf.readBlockPos());
    }

    public BlockPos getArkPos() {
        return arkPos;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    // =========================
    // ★ 蘇生処理（サーバー側）
    // =========================
    public void attemptResurrect(int index, ServerPlayerEntity player) {

        final int COST = 32;

        if (!hasEmeralds(player, COST)) {
            player.sendMessage(Text.literal("エメラルドが足りません（必要: 32）"), false);
            return;
        }

        removeEmeralds(player, COST);

        // TODO: 次のステップで Mob 蘇生処理を実装
        player.sendMessage(Text.literal("蘇生しました！（仮）"), false);
    }

    private boolean hasEmeralds(ServerPlayerEntity player, int amount) {
        int count = 0;
        for (ItemStack stack : player.getInventory().main) {
            if (stack.getItem() == Items.EMERALD) {
                count += stack.getCount();
                if (count >= amount) return true;
            }
        }
        return false;
    }

    private void removeEmeralds(ServerPlayerEntity player, int amount) {
        int remaining = amount;

        for (int i = 0; i < player.getInventory().main.size(); i++) {
            ItemStack stack = player.getInventory().main.get(i);
            if (stack.getItem() != Items.EMERALD) continue;

            int remove = Math.min(stack.getCount(), remaining);
            stack.decrement(remove);
            remaining -= remove;

            if (remaining <= 0) break;
        }
    }
}