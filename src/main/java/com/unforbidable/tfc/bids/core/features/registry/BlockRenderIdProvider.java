package com.unforbidable.tfc.bids.core.features.registry;

import java.util.HashMap;
import java.util.Map;

public class BlockRenderIdProvider {

    final static Map<String, Integer> blockRenderIds = new HashMap<>();

    public static int get(String name) {
        return blockRenderIds.get(name);
    }

}
