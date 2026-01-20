package com.licht_meilleur.resurrection_ark.event;

import com.licht_meilleur.resurrection_ark.ArkLinkState;
import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MobRegisterEvent {

    public static void register() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;
            if (!(world instanceof ServerWorld serverWorld)) return ActionResult.PASS;

            // ✅ しゃがみ+右クリック時だけ
            if (!player.isSneaking()) return ActionResult.PASS;

            // ✅ Mob（LivingEntity）だけ対象
            if (!(entity instanceof LivingEntity living)) return ActionResult.PASS;

            // ✅ リンク先Arkがあるか？
            BlockPos arkPos = ArkLinkState.get(player.getUuid());
            if (arkPos == null) return ActionResult.PASS;

            // ✅ Ark BlockEntity 取得
            if (!(serverWorld.getBlockEntity(arkPos) instanceof ResurrectionArkBlockEntity arkBe)) {
                ArkLinkState.clear(player.getUuid());
                player.sendMessage(Text.literal("リンク先Arkが見つかりません。もう一度リンクしてください。"), false);
                return ActionResult.CONSUME;
            }

            // ✅ 所有判定（まずはTameableEntityだけ厳密チェック）
            if (living instanceof TameableEntity tame) {
                if (tame.getOwnerUuid() == null || !tame.getOwnerUuid().equals(player.getUuid())) {
                    player.sendMessage(Text.literal("このMobの主があなたではありません。"), false);
                    return ActionResult.CONSUME;
                }
            }
            // それ以外のMobは「プレイヤーが登録したらOK」方針

            // ✅ 登録
            boolean ok = arkBe.addMobFromEntity(living, (PlayerEntity) player);
            if (ok) {
                player.sendMessage(Text.literal("MobをArkに登録しました。"), false);
            } else {
                player.sendMessage(Text.literal("登録に失敗しました（上限/不正データなど）。"), false);
            }

            // ✅ リンク解除（1回登録したら解除）
            ArkLinkState.clear(player.getUuid());

            return ActionResult.CONSUME;
        });
    }
}