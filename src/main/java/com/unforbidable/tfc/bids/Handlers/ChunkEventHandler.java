package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.Core.Chunk.ChunkState;
import com.unforbidable.tfc.bids.Core.Chunk.ChunkStateManager;
import com.unforbidable.tfc.bids.WorldGen.CropWorldGen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.ChunkEvent;

public class ChunkEventHandler {

    @SubscribeEvent
    public void onLoad(ChunkEvent.Load event) {
        if (!event.world.isRemote) {
            ChunkState state = ChunkStateManager.getChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
            int month = TFC_Time.getSeasonAdjustedMonth(event.getChunk().zPosition << 4);

            if (state.getLastSpringGenYear() < TFC_Time.getYear() && month > TFC_Time.APRIL && month < TFC_Time.SEPTEMBER) {
                CropWorldGen gen = new CropWorldGen();
                gen.generateCrops(event.world, event.world.rand, event.getChunk().xPosition, event.getChunk().zPosition, true);

                state.setLastSpringGenYear(TFC_Time.getYear());

                event.getChunk().isModified = true;
            }
        }
    }

    @SubscribeEvent
    public void onDataLoad(ChunkDataEvent.Load event) {
        if (!event.world.isRemote) {
            NBTTagCompound eventTag = event.getData();
            ChunkState state = ChunkState.readChunkStateFromNBT(eventTag);
            ChunkStateManager.setChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition, state);
        }
    }

    @SubscribeEvent
    public void onDataSave(ChunkDataEvent.Save event) {
        if (!event.world.isRemote) {
            NBTTagCompound eventTag = event.getData();
            ChunkState state = ChunkStateManager.getChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
            if (state != null) {
                state.writeChunkStateToNBT(eventTag);

                if (!event.getChunk().isChunkLoaded) {
                    ChunkStateManager.clearChunkState(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
                }
            }
        }
    }

}
