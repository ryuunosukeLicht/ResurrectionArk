package com.licht_meilleur.resurrection_ark.screen;

import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static ScreenHandlerType<ResurrectionArkScreenHandler> RESURRECTION_ARK;

    public static void register() {
        RESURRECTION_ARK = ScreenHandlerRegistry.registerSimple(
                new Identifier(ResurrectionArkMod.MOD_ID, "resurrection_ark"),
                ResurrectionArkScreenHandler::new
        );
    }
}
