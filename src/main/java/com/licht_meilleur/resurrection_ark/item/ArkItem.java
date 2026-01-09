package com.licht_meilleur.resurrection_ark.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ArkItem extends Item {
    public ArkItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            // GUI の開閉は packet 経由で
            ResurrectionArkPackets.sendOpenMobListPacket();
        }
        return TypedActionResult.success(user.getStackInHand(hand));
    }
}