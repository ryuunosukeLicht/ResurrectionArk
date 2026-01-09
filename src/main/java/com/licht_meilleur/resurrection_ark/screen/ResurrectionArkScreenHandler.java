package com.licht_meilleur.resurrection_ark.screen;

import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ResurrectionArkScreenHandler extends ScreenHandler {

    public final ResurrectionArkBlockEntity blockEntity;

    public ResurrectionArkScreenHandler(int syncId, PlayerInventory playerInventory,
                                        ResurrectionArkBlockEntity blockEntity) {
        super(ModScreenHandlers.RESURRECTION_ARK, syncId);
        this.blockEntity = blockEntity;

        // プレイヤーインベントリを追加
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    public ResurrectionArkScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, null);
    }
}