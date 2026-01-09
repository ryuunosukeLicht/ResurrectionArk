package com.licht_meilleur.resurrection_ark;


import com.licht_meilleur.resurrection_ark.screen.ModScreenHandlers;
import com.licht_meilleur.resurrection_ark.screen.ResurrectionArkScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ResurrectionArkModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        ScreenRegistry.register(ModScreenHandlers.RESURRECTION_ARK, ResurrectionArkScreen::new);
        ResurrectionArkPackets.registerClientReceiver();
    }

}