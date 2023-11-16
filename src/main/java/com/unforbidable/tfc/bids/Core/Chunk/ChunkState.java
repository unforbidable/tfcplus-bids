package com.unforbidable.tfc.bids.Core.Chunk;

import net.minecraft.nbt.NBTTagCompound;

public class ChunkState {

    private static final String TAG_CHUNK_STATE = "BidsChunkState";
    private static final String TAG_LAST_SPRING_GEN_YEAR = "LastSpringGenYear";

    private int lastSpringGenYear = 0;

    public int getLastSpringGenYear() {
        return lastSpringGenYear;
    }

    public void setLastSpringGenYear(int lastSpringGenYear) {
        this.lastSpringGenYear = lastSpringGenYear;
    }

    public static ChunkState readChunkStateFromNBT(NBTTagCompound eventTag) {
        NBTTagCompound tagChunkState = eventTag.getCompoundTag(TAG_CHUNK_STATE);

        ChunkState state = new ChunkState();
        if (tagChunkState.hasKey(TAG_LAST_SPRING_GEN_YEAR)) {
            state.lastSpringGenYear = tagChunkState.getInteger(TAG_LAST_SPRING_GEN_YEAR);
        }

        return state;
    }

    public void writeChunkStateToNBT(NBTTagCompound eventTag) {
        NBTTagCompound tagChunkState = new NBTTagCompound();

        tagChunkState.setInteger(TAG_LAST_SPRING_GEN_YEAR, lastSpringGenYear);

        eventTag.setTag(TAG_CHUNK_STATE, tagChunkState);
    }

}
