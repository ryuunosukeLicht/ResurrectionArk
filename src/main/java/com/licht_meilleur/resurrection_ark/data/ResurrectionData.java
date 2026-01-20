package com.licht_meilleur.resurrection_ark.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ResurrectionData extends PersistentState {
    private final Map<UUID, String> linkedMobs = new HashMap<>();
    private final Set<UUID> registeringPlayers = new HashSet<>();

    public void registerMob(UUID mobId, String typeId) {
        if (linkedMobs.size() < 100) {
            linkedMobs.put(mobId, typeId);
            markDirty();
        }
    }

    public Map<UUID, String> getAllMobs() {
        return linkedMobs;
    }

    /**
     * プレイヤーが現在登録モードかを返す
     */
    public boolean isRegisterMode(UUID playerId) {
        return registeringPlayers.contains(playerId);
    }

    /**
     * プレイヤーの登録モードを設定する
     * 変化があれば markDirty() を呼ぶ
     */
    public void setRegisterMode(UUID playerId, boolean flag) {
        boolean changed;
        if (flag) {
            changed = registeringPlayers.add(playerId);
        } else {
            changed = registeringPlayers.remove(playerId);
        }
        if (changed) {
            markDirty();
        }
    }

    public boolean remove(UUID id) {
        if (this.linkedMobs.remove(id) != null) {
            this.markDirty();
            return true;
        }
        return false;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        NbtCompound mobTag = new NbtCompound();
        for (Map.Entry<UUID, String> e : linkedMobs.entrySet()) {
            mobTag.putString(e.getKey().toString(), e.getValue());
        }
        nbt.put("Mobs", mobTag);
        return nbt;
    }

    public static ResurrectionData readNbt(NbtCompound nbt) {
        ResurrectionData data = new ResurrectionData();
        NbtCompound mobTag = nbt.getCompound("Mobs");
        for (String key : mobTag.getKeys()) {
            data.linkedMobs.put(UUID.fromString(key), mobTag.getString(key));
        }
        return data;
    }

    public static ResurrectionData get(ServerWorld world) {
        return world.getPersistentStateManager().getOrCreate(
                ResurrectionData::readNbt,
                ResurrectionData::new,
                "resurrection_data"
        );
    }

}