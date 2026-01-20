package com.licht_meilleur.resurrection_ark.block;

import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {

    public static final Block RESURRECTION_ARK_BLOCK = registerBlockWithItem(
            "resurrection_ark",
            new ResurrectionArkBlock()
    );

    // ブロック + BlockItem を同じIDで登録するヘルパー
    private static Block registerBlockWithItem(String name, Block block) {
        Identifier id = new Identifier(ResurrectionArkMod.MOD_ID, name);

        // ブロック登録
        Block registeredBlock = Registry.register(Registries.BLOCK, id, block);

        // ブロックアイテム登録（同じID）
        Registry.register(Registries.ITEM, id, new BlockItem(registeredBlock, new Item.Settings()));

        return registeredBlock;
    }

    public static void registerAll() {
        ResurrectionArkMod.LOGGER.info("Registering ModBlocks for " + ResurrectionArkMod.MOD_ID);
        // ここは「呼び出し用」(静的初期化で登録自体は終わってる)
    }
}