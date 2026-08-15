package com.unforbidable.tfc.bids.core.chunk;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;

public class ChunkEventHandler {

    @SubscribeEvent
    public void onLoad(ChunkEvent.Load event) {
        if (!event.world.isRemote) {
        }
    }

    @SubscribeEvent
    public void onDataLoad(ChunkDataEvent.Load event) {
        if (!event.world.isRemote) {
            ChunkData state = ChunkDataManager.getChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
            state.readChunkStateFromNBT(event.getData());
            ChunkDataManager.setChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition, state);
        }
    }

    @SubscribeEvent
    public void onDataSave(ChunkDataEvent.Save event) {
        if (!event.world.isRemote) {
            ChunkData state = ChunkDataManager.getChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
            state.writeChunkStateToNBT(event.getData());
            if (!event.getChunk().isChunkLoaded) {
                ChunkDataManager.clearChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
            }
        }
    }

}
