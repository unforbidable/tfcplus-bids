package com.unforbidable.tfc.bids.core.chunk;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class ChunkData {

    private static final String TAG_CHUNK_STATE = "BidsChunkState";

    private final Map<Class<? extends ChunkDataValue>, ChunkDataValue> data = new HashMap<>();

    public static ChunkData of(Chunk chunk) {
        return ChunkDataManager.getChunkState(chunk.worldObj, chunk.xPosition, chunk.zPosition);
    }

    public static ChunkData of(World world, int x, int z) {
        return ChunkDataManager.getChunkState(world, x, z);
    }

    public ChunkData() {
        for (Class<? extends ChunkDataValue> type : ChunkRegistry.data) {
            ChunkDataValue value = createDataValueInstance(type);
            data.put(type, value);
        }
    }

    void readChunkStateFromNBT(NBTTagCompound eventTag) {
        if (eventTag.hasKey(TAG_CHUNK_STATE)) {
            NBTTagCompound tagChunkState = eventTag.getCompoundTag(TAG_CHUNK_STATE);

            for (ChunkDataValue value : data.values()) {
                value.readChunkStateValue(tagChunkState);
            }
        }
    }

    private static ChunkDataValue createDataValueInstance(Class<? extends ChunkDataValue> type) {
        try {
            return type.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    void writeChunkStateToNBT(NBTTagCompound eventTag) {
        NBTTagCompound tagChunkState = eventTag.getCompoundTag(TAG_CHUNK_STATE);

        for (ChunkDataValue value : data.values()) {
            value.writeChunkStateValue(tagChunkState);
        }

        if (!tagChunkState.hasNoTags()) {
            eventTag.setTag(TAG_CHUNK_STATE, tagChunkState);
        } else if (eventTag.hasKey(TAG_CHUNK_STATE)) {
            eventTag.removeTag(TAG_CHUNK_STATE);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends ChunkDataValue> T get(Class<? extends T> type) {
        if (data.containsKey(type)) {
            return (T) data.get(type);
        } else {
            throw new RuntimeException(MessageFormat.format("Chunk data value of type {} is not registered", type.getCanonicalName()));
        }
    }

}
