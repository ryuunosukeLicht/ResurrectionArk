package com.licht_meilleur.resurrection_ark.block.entity;

import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import com.licht_meilleur.resurrection_ark.block.ModBlocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {

    public static BlockEntityType<ResurrectionArkBlockEntity> RESURRECTION_ARK_ENTITY_TYPE;

    public static void registerAll() {
        RESURRECTION_ARK_ENTITY_TYPE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier(ResurrectionArkMod.MOD_ID, "resurrection_ark_entity"),
                BlockEntityType.Builder.create(ResurrectionArkBlockEntity::new, ModBlocks.RESURRECTION_ARK_BLOCK).build(null)
        );
    }
}