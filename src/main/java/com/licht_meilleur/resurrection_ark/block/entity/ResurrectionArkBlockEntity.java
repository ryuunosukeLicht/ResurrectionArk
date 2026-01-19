package com.licht_meilleur.resurrection_ark.block.entity;

import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import com.licht_meilleur.resurrection_ark.data.ResurrectionData;
import com.licht_meilleur.resurrection_ark.screen.ResurrectionArkScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ResurrectionArkBlockEntity extends BlockEntity implements NamedScreenHandlerFactory {
    public ResurrectionArkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESURRECTION_ARK_ENTITY_TYPE, pos, state); // `ModBlockEntities` で登録済みとする
    }

    @Override
    public Text getDisplayName() {
        return Text.translatable("container.resurrection_ark");
    }

    @Override
    public ResurrectionArkScreenHandler createMenu(int syncId, PlayerInventory inv, net.minecraft.entity.player.PlayerEntity player) {
        World world = this.getWorld();
        // ScreenHandler に world/pos を渡してサーバ側で ResurrectionData を操作する
        return new ResurrectionArkScreenHandler(syncId, inv, (ServerWorld) world, this.pos);
    }
}