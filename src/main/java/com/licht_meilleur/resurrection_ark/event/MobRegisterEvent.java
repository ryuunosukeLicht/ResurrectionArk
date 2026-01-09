package com.licht_meilleur.resurrection_ark.event;

import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import com.licht_meilleur.resurrection_ark.data.ResurrectionData;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.EntityType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MobRegisterEvent {
    public static void register() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;

            // 近くに登録モード中のArkブロックがあるか探す
            for (BlockPos pos : BlockPos.iterateOutwards(entity.getBlockPos(), 5, 5, 5)) {
                if (world.getBlockEntity(pos) instanceof ResurrectionArkBlockEntity arkBe && arkBe.isRegistering()) {
                    ResurrectionData data = ResurrectionData.get(world);

                    data.registerMob(
                            entity.getUuid(),
                            EntityType.getId(entity.getType()).toString()
                    );

                    player.sendMessage(Text.literal(
                            "Mobを登録しました: " + entity.getName().getString()
                    ), false);

                    arkBe.setRegistering(false); // 登録モード解除
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        });
    }
}