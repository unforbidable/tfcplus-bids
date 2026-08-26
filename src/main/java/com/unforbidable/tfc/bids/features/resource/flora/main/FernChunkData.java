package com.unforbidable.tfc.bids.features.resource.flora.main;

import com.unforbidable.tfc.bids.core.chunk.ChunkDataValue;
import net.minecraft.nbt.NBTTagCompound;

public class FernChunkData implements ChunkDataValue {

    private static final String TAG_NEXT_GROWTH_DAY = "NextGrowthDay";

    public int nextGrowthDay = 0;

    @Override
    public void readChunkStateValue(NBTTagCompound tag) {
        if (tag.hasKey(TAG_NEXT_GROWTH_DAY)) {
            nextGrowthDay = tag.getInteger(TAG_NEXT_GROWTH_DAY);
        }
    }

    @Override
    public void writeChunkStateValue(NBTTagCompound tag) {
        if (nextGrowthDay != 0) {
            tag.setInteger(TAG_NEXT_GROWTH_DAY, nextGrowthDay);
        }
    }

}
