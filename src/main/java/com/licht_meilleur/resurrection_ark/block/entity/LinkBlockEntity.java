package com.licht_meilleur.resurrection_ark.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.BlockState;

import java.util.UUID;

public class LinkBlockEntity extends BlockEntity {
    private UUID owner;
    private UUID linkedMob;

    public LinkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.LINK_BLOCK_ENTITY, pos, state);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
        markDirty(); // Fabricでデータ更新を通知
    }

    public void setLinkedMob(UUID mob) {
        this.linkedMob = mob;
        markDirty();
    }

    public UUID getOwner() { return owner; }
    public UUID getLinkedMob() { return linkedMob; }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        // 独自データを保存したい場合はここに書く
        // 例: nbt.putInt("value", this.value);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        // 独自データを読み込む場合はここに書く
        // 例: this.value = nbt.getInt("value");
    }
}