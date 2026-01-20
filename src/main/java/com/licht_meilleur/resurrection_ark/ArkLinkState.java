package com.licht_meilleur.resurrection_ark;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ArkLinkState {
    private static final Map<UUID, BlockPos> LINKED_ARK = new HashMap<>();

    public static void set(UUID playerId, BlockPos pos) {
        LINKED_ARK.put(playerId, pos);
    }

    public static BlockPos get(UUID playerId) {
        return LINKED_ARK.get(playerId);
    }

    public static void clear(UUID playerId) {
        LINKED_ARK.remove(playerId);
    }
}