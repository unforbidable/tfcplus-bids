package com.unforbidable.tfc.bids.features.resource.crop.main;

import com.unforbidable.tfc.bids.core.chunk.ChunkDataValue;
import net.minecraft.nbt.NBTTagCompound;

public class CropChunkData implements ChunkDataValue {

    private static final String TAG_LAST_SPRING_GEN_YEAR = "LastSpringGenYear";

    public int lastSpringGenYear = 0;

    @Override
    public void readChunkStateValue(NBTTagCompound tag) {
        if (tag.hasKey(TAG_LAST_SPRING_GEN_YEAR)) {
            lastSpringGenYear = tag.getInteger(TAG_LAST_SPRING_GEN_YEAR);
        }
    }

    @Override
    public void writeChunkStateValue(NBTTagCompound tag) {
        if (lastSpringGenYear != 0) {
            tag.setInteger(TAG_LAST_SPRING_GEN_YEAR, lastSpringGenYear);
        }
    }

}
