package com.licht_meilleur.resurrection_ark;

import com.licht_meilleur.resurrection_ark.screen.MobListScreen;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;

public final class ResurrectionArkPackets {
    public static final Identifier OPEN_MOB_LIST = new Identifier(ResurrectionArkMod.MOD_ID, "open_mob_list");

    public static void registerClientReceiver() {
        ClientPlayNetworking.registerGlobalReceiver(OPEN_MOB_LIST, (client, handler, buf, responseSender) -> {
            client.execute(() -> {
                MinecraftClient.getInstance().setScreen(new MobListScreen());
            });
        });
    }

    public static void sendOpenMobListPacket() {
        ClientPlayNetworking.send(OPEN_MOB_LIST, PacketByteBufs.create());
    }
}