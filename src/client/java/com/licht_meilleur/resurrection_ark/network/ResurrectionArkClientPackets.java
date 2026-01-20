package com.licht_meilleur.resurrection_ark.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public class ResurrectionArkClientPackets {

    public static final Identifier RESURRECT =
            new Identifier("resurrection_ark", "resurrect");

    public static void sendResurrectRequest(int index) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(index);
        ClientPlayNetworking.send(RESURRECT, buf);
    }
}