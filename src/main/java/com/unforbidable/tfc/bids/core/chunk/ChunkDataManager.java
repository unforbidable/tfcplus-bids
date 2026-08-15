package com.unforbidable.tfc.bids.core.chunk;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.world.World;

public class ChunkDataManager {

    private static final Map<Integer, Map<Long, ChunkData>> worlds = new HashMap<>();

    static void setChunkState(World world, int xPosition, int zPosition, ChunkData state) {
        long chunkKey = getChunkPositionKey(xPosition, zPosition);

        if (worlds.containsKey(world.provider.dimensionId)) {
            worlds.get(world.provider.dimensionId).put(chunkKey, state);
        } else {
            Map<Long, ChunkData> chunks = new HashMap<Long, ChunkData>();
            chunks.put(chunkKey, state);
            worlds.put(world.provider.dimensionId, chunks);
        }
    }

    static ChunkData getChunkState(World world, int xPosition, int zPosition) {
        long chunkKey = getChunkPositionKey(xPosition, zPosition);

        if (worlds.containsKey(world.provider.dimensionId)) {
            Map<Long, ChunkData> chunks = worlds.get(world.provider.dimensionId);
            if (chunks.containsKey(chunkKey)) {
                return chunks.get(chunkKey);
            } else {
                ChunkData state = new ChunkData();
                chunks.put(chunkKey, state);
                return state;
            }
        } else {
            ChunkData state = new ChunkData();
            Map<Long, ChunkData> chunks = new HashMap<Long, ChunkData>();
            chunks.put(chunkKey, state);
            worlds.put(world.provider.dimensionId, chunks);
            return state;
        }
    }

    static void clearChunkState(World world, int xPosition, int zPosition) {
        long chunkKey = getChunkPositionKey(xPosition, zPosition);

        if (worlds.containsKey(world.provider.dimensionId)) {
            Map<Long, ChunkData> chunks = worlds.get(world.provider.dimensionId);
            chunks.remove(chunkKey);
        }
    }

    private static long getChunkPositionKey(int xPosition, int zPosition) {
        return (long) xPosition * Integer.MAX_VALUE + zPosition;
    }

}
