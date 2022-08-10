package com.unforbidable.tfc.bids.api;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;

public class QuarryRegistry {

    static final Map<Block, IQuarriable> quarries = new HashMap<Block, IQuarriable>();

    public static void registerQuarryBlock(IQuarriable quarriable) {
        quarries.put(quarriable.getRawBlock(), quarriable);
    }

    public static IQuarriable getBlockQuarriable(Block rawBlock) {
        return quarries.get(rawBlock);
    }

    public static boolean isBlockQuarriable(Block rawStoneBlock) {
        return getBlockQuarriable(rawStoneBlock) != null;
    }

    public static List<IQuarriable> getQuarriableBlocks() {
        return new ArrayList<IQuarriable>(quarries.values());
    }

}
