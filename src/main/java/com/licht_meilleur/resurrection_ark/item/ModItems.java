package com.licht_meilleur.resurrection_ark.item;


import com.licht_meilleur.resurrection_ark.ResurrectionArkMod;
import com.licht_meilleur.resurrection_ark.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {
    // 中間アイテム Resurrection Cross
    public static final Item RESURRECTION_CROSS = registerItem("resurrection_cross",
            new Item(new Item.Settings()));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(ResurrectionArkMod.MOD_ID, name), item);
    }

    public static void registerAll() {
        System.out.println("Registering ModItems for " + ResurrectionArkMod.MOD_ID);

        // クリエイティブタブに追加
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(RESURRECTION_CROSS);
        });

    }
}