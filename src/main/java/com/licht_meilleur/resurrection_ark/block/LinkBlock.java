package com.licht_meilleur.resurrection_ark.block;

import com.licht_meilleur.resurrection_ark.block.entity.LinkBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class LinkBlock {

    public ActionResult onUse(BlockState state, World world, BlockPos pos,
                              PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof LinkBlockEntity linkBe) {
            if (player.isSneaking()) {
                // 所有者を登録
                linkBe.setOwner(player.getUuid());
                player.sendMessage(Text.literal("このブロックを所有者として登録しました"), false);
                return ActionResult.SUCCESS;
            } else {
                // 所有者情報を表示
                UUID owner = linkBe.getOwner();
                UUID mob = linkBe.getLinkedMob();
                player.sendMessage(Text.literal("Owner: " + owner), false);
                player.sendMessage(Text.literal("Linked Mob: " + mob), false);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }
}
