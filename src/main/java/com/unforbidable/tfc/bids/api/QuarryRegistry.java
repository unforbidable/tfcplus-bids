package com.unforbidable.tfc.bids.api;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.api.Interfaces.IQuarriable;

import net.minecraft.block.Block;

public class QuarryRegistry {

    static final List<IQuarriable> quarries = new ArrayList<IQuarriable>();

    public static void registerQuarryBlock(IQuarriable quarriable) {
        quarries.add(quarriable);
    }

    public static IQuarriable getBlockQuarriable(Block rawBlock) {
        for (IQuarriable q : quarries) {
            if (q.getRawBlock() == rawBlock) {
                return q;
            }
        }

        return null;
    }

    public static boolean isBlockQuarriable(Block rawStoneBlock) {
        return getBlockQuarriable(rawStoneBlock) != null;
    }

}
