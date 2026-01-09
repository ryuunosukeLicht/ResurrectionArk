package com.licht_meilleur.resurrection_ark.block;

import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block RESURRECTION_ARK = registerBlock("resurrection_ark", new ResurrectionArkBlock());

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(ResurrectionArkMod.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, new Identifier(ResurrectionArkMod.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    public static void registerAll() {
        System.out.println("Registering ModBlocks for " + ResurrectionArkMod.MOD_ID);
    }
}