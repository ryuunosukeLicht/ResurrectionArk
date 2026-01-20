package com.licht_meilleur.resurrection_ark.screen;

import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;

public class ResurrectionArkScreenHandler extends ScreenHandler {

    private final ResurrectionArkBlockEntity blockEntity;

    // ★ BlockEntity から呼ばれる本命コンストラクタ
    public ResurrectionArkScreenHandler(int syncId, PlayerInventory inv, ResurrectionArkBlockEntity be) {
        super(ModScreenHandlers.RESURRECTION_ARK_SCREEN_HANDLER, syncId);
        this.blockEntity = be;
    }

    // ★ ScreenHandlerType 登録用のダミー（重要）
    public ResurrectionArkScreenHandler(int syncId, PlayerInventory inv) {
        this(syncId, inv, null);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}