package com.licht_meilleur.resurrection_ark.network;

import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import com.licht_meilleur.resurrection_ark.block.entity.ResurrectionArkBlockEntity;
import com.licht_meilleur.resurrection_ark.screen.ResurrectionArkScreenHandler;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.text.Text;

public class ResurrectionArkServerPackets {

    public static final Identifier RESURRECT =
            new Identifier("resurrection_ark", "resurrect");

    public static final Identifier DELETE_MOB = new Identifier(ResurrectionArkMod.MOD_ID, "delete_mob");
    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
                RESURRECT,
                (server, player, handler, buf, responseSender) -> {

                    int idx = buf.readInt();

                    server.execute(() -> {
                        if (player.currentScreenHandler
                                instanceof ResurrectionArkScreenHandler screenHandler) {
                            screenHandler.attemptResurrect(idx, player);
                        }
                    });
                }
        );
        ServerPlayNetworking.registerGlobalReceiver(DELETE_MOB, (server, player, handler, buf, responseSender) -> {
            BlockPos pos = buf.readBlockPos();
            int index = buf.readInt();

            server.execute(() -> {
                if (!(player.getWorld().getBlockEntity(pos) instanceof ResurrectionArkBlockEntity arkBe)) return;

                boolean ok = arkBe.removeMob(index);
                if (ok) {
                    player.sendMessage(Text.literal("登録を削除しました。"), false);
                } else {
                    player.sendMessage(Text.literal("削除に失敗しました。"), false);
                }
            });
        });

    }

}