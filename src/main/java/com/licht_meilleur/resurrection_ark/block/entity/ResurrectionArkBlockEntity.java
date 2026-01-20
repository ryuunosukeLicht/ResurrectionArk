package com.licht_meilleur.resurrection_ark.block.entity;

import com.licht_meilleur.resurrection_ark.screen.ResurrectionArkScreenHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.network.PacketByteBuf;
import java.util.UUID;

import java.util.ArrayList;
import java.util.List;

public class ResurrectionArkBlockEntity extends BlockEntity
        implements net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory{

    public ResurrectionArkBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.RESURRECTION_ARK_ENTITY_TYPE, pos, state);
    }

    /* =========================
       GUI タイトル
       ========================= */

    @Override
    public Text getDisplayName() {
        return Text.literal("Resurrection Ark");
    }

    /* =========================
       ScreenHandler 生成
       ========================= */

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
        return new ResurrectionArkScreenHandler(syncId, inventory, this.pos);
    }

    public static class StoredMob {
        public Identifier typeId;
        public String name;
        public NbtCompound data;

        // ★重複防止用（元MobのUUID）
        public UUID sourceUuid;

        public UUID mobUuid;

        public StoredMob(Identifier typeId, String name, NbtCompound data, UUID sourceUuid) {
            this.typeId = typeId;
            this.name = name;
            this.data = data;
            this.sourceUuid = sourceUuid;
        }
    }

    private final List<StoredMob> storedMobs = new ArrayList<>();
    private static final int MAX_MOBS = 64;

    // GUIで読む用
    public List<StoredMob> getStoredMobs() {
        return storedMobs;
    }
    public boolean addMobFromEntity(LivingEntity entity, PlayerEntity player) {
        if (this.world == null || this.world.isClient) return false;
        if (storedMobs.size() >= MAX_MOBS) return false;

        UUID srcUuid = entity.getUuid();

        // ✅ すでに同じUUIDが登録済みなら弾く
        for (StoredMob m : storedMobs) {
            if (m.sourceUuid != null && m.sourceUuid.equals(srcUuid)) {
                player.sendMessage(net.minecraft.text.Text.literal("このMobはすでに登録済みです。"), false);
                return false;
            }
        }

        Identifier id = Registries.ENTITY_TYPE.getId(entity.getType());
        if (id == null) return false;

        NbtCompound nbt = new NbtCompound();
        entity.writeNbt(nbt);

        // ★UUIDは「重複判定」と「表示用」に必要なので消さない
        // nbt.remove("UUID"); ← これは削除/コメントアウト

        // 位置/速度などは消してOK
        nbt.remove("Pos");
        nbt.remove("Motion");
        nbt.remove("Rotation");
        nbt.remove("FallDistance");
        nbt.remove("Fire");
        nbt.remove("Air");
        nbt.remove("OnGround");
        nbt.remove("PortalCooldown");
        nbt.remove("Passengers");
        nbt.remove("Leash");

        String displayName = entity.getName().getString();

        storedMobs.add(new StoredMob(id, displayName, nbt, srcUuid));

        markDirtyAndSync();
        return true;
    }

    private void markDirtyAndSync() {
        this.markDirty();
        if (this.world != null && !this.world.isClient) {
            BlockState state = this.getCachedState();
            this.world.updateListeners(this.pos, state, state, 3);
        }
    }
    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        NbtList list = new NbtList();


        for (StoredMob mob : storedMobs) {
            NbtCompound e = new NbtCompound();
            e.putString("Type", mob.typeId.toString());
            e.putString("Name", mob.name);
            e.put("Data", mob.data);
            list.add(e);
            if (mob.sourceUuid != null) {
                e.putUuid("SourceUUID", mob.sourceUuid);
            }
        }
        nbt.put("StoredMobs", list);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        storedMobs.clear();
        NbtList list = nbt.getList("StoredMobs", 10); // 10=Compound
        for (int i = 0; i < list.size(); i++) {
            NbtCompound e = list.getCompound(i);
            Identifier typeId = new Identifier(e.getString("Type"));
            String name = e.getString("Name");
            NbtCompound data = e.getCompound("Data");
            UUID src = e.containsUuid("SourceUUID") ? e.getUuid("SourceUUID") : null;
            storedMobs.add(new StoredMob(typeId, name, data, src));
        }
    }
    @Override
    public NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = new NbtCompound();
        this.writeNbt(nbt);
        return nbt;
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }
    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
    public boolean removeMob(int index) {
        if (this.world == null || this.world.isClient) return false;
        if (index < 0 || index >= storedMobs.size()) return false;

        storedMobs.remove(index);
        markDirtyAndSync();
        return true;
    }


}