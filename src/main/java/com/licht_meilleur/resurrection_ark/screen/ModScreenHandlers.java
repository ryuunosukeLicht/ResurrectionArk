package com.licht_meilleur.resurrection_ark.screen;

import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {
    public static ScreenHandlerType<ResurrectionArkScreenHandler> RESURRECTION_ARK_SCREEN_HANDLER;

    public static void registerAll() {
        RESURRECTION_ARK_SCREEN_HANDLER = Registry.register(
                Registries.SCREEN_HANDLER,
                new Identifier(ResurrectionArkMod.MOD_ID, "resurrection_ark"),
                new ScreenHandlerType<>(ResurrectionArkScreenHandler::new)
        );
    }
}