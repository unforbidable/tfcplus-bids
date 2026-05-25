package com.unforbidable.tfc.bids.core.features.registry;

import com.unforbidable.tfc.bids.Bids;
import net.minecraft.block.Block;
import java.util.HashMap;
import java.util.Map;

public class BlockRenderIdProvider {

    final static Map<Class<? extends Block>, Integer> blockRenderIds = new HashMap<>();

    public static int get(Block block) {
        Integer id = blockRenderIds.get(block.getClass());
        if (id != null) {
            return id;
        } else {
            Bids.LOG.warn("Block type {} not registered in render ID provider", block.getClass().getCanonicalName());

            return 0;
        }
    }

}
