package com.unforbidable.tfc.bids.core.chunk;

import net.minecraft.nbt.NBTTagCompound;

public interface ChunkDataValue {

    void readChunkStateValue(NBTTagCompound tag);
    void writeChunkStateValue(NBTTagCompound tag);

}
