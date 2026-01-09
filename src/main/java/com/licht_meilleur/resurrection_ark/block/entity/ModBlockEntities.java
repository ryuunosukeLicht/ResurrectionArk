package com.licht_meilleur.resurrection_ark.block.entity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import com.licht_meilleur.resurrection_ark.block.ModBlocks;

public class ModBlockEntities {
    public static BlockEntityType<LinkBlockEntity> LINK_BLOCK_ENTITY;

    public static void registerAll() {
        LINK_BLOCK_ENTITY = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                new Identifier("resurrection_ark", "link_block_entity"),
                BlockEntityType.Builder.create(LinkBlockEntity::new, ModBlocks.LINK_BLOCK).build(null)
        );
    }
}