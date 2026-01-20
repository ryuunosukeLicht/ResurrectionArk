package com.licht_meilleur.resurrection_ark.screen;

import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

public class ResurrectionArkScreenHandler extends ScreenHandler {

    private final BlockPos arkPos;

    // サーバ側（BlockEntity#createMenu から）
    public ResurrectionArkScreenHandler(int syncId, PlayerInventory inv) {
        super(ModScreenHandlers.RESURRECTION_ARK, syncId);
        this.arkPos = BlockPos.ORIGIN; // サーバ側ではScreen側表示に使わない
    }

    // クライアント側（ExtendedScreenHandlerType から）
    public ResurrectionArkScreenHandler(int syncId, PlayerInventory inv, PacketByteBuf buf) {
        super(ModScreenHandlers.RESURRECTION_ARK, syncId);
        this.arkPos = buf.readBlockPos();
    }

    public BlockPos getArkPos() {
        return arkPos;
    }

    /**
     * サーバ側で蘇生を試みる入口。
     * ResurrectionArkServerPackets から呼ばれる想定。
     *
     * ※ まだ中身はダミーでOK。次で「エメラルド32消費→NBTからスポーン」を入れる。
     */
    public void attemptResurrect(int index, ServerPlayerEntity player) {
        if (player.getWorld().isClient) return;

        BlockPos pos = player.getBlockPos(); // fallback
        // サーバ側でも arkPos を使いたい場合は、ScreenHandler生成を「posを渡す方式」に変える必要がある。
        // いまは「コンパイルを通す＋次の実装の土台」なので、ここは後で置き換える。

        player.sendMessage(net.minecraft.text.Text.literal("[ResurrectionArk] attemptResurrect index=" + index), false);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }
}