package com.unforbidable.tfc.bids.Core.Chunk;

import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ChunkStateManager {

    private static final Map<World, Map<Long, ChunkState>> worlds = new HashMap<World, Map<Long, ChunkState>>();

    public static void setChunkState(World world, int xPosition, int zPosition, ChunkState state) {
        long chunkKey = getChunkPositionKey(xPosition, zPosition);

        if (worlds.containsKey(world)) {
            worlds.get(world).put(chunkKey, state);
        } else {
            Map<Long, ChunkState> chunks = new HashMap<Long, ChunkState>();
            chunks.put(chunkKey, state);
            worlds.put(world, chunks);
        }
    }

    public static ChunkState getChunkState(World world, int xPosition, int zPosition) {
        long chunkKey = getChunkPositionKey(xPosition, zPosition);

        if (worlds.containsKey(world)) {
            Map<Long, ChunkState> chunks = worlds.get(world);
            if (chunks.containsKey(chunkKey)) {
                return chunks.get(chunkKey);
            } else {
                ChunkState state = new ChunkState();
                chunks.put(chunkKey, state);
                return state;
            }
        } else {
            ChunkState state = new ChunkState();
            Map<Long, ChunkState> chunks = new HashMap<Long, ChunkState>();
            chunks.put(chunkKey, state);
            worlds.put(world, chunks);
            return state;
        }
    }

    public static void clearChunkState(World world, int xPosition, int zPosition) {
        long chunkKey = getChunkPositionKey(xPosition, zPosition);

        if (worlds.containsKey(world)) {
            Map<Long, ChunkState> chunks = worlds.get(world);
            chunks.remove(chunkKey);
        }
    }

    private static long getChunkPositionKey(int xPosition, int zPosition) {
        return (long) xPosition * Integer.MAX_VALUE + zPosition;
    }

}
