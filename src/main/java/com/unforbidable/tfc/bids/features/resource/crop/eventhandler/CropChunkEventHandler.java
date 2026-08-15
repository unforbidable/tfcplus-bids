package com.unforbidable.tfc.bids.features.resource.crop.eventhandler;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.core.chunk.ChunkData;
import com.unforbidable.tfc.bids.features.resource.crop.main.CropChunkData;
import com.unforbidable.tfc.bids.features.resource.crop.worldgen.CropWorldGen;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.event.world.ChunkEvent;

public class CropChunkEventHandler {

    @SubscribeEvent
    public void onLoad(ChunkEvent.Load event) {
        if (!event.world.isRemote) {
            CropChunkData cropChunkData = ChunkData.of(event.getChunk()).get(CropChunkData.class);
            if (cropChunkData.lastSpringGenYear > 0) {
                int month = TFC_Time.getSeasonAdjustedMonth(event.getChunk().zPosition << 4);

                if (cropChunkData.lastSpringGenYear < TFC_Time.getYear() && month > TFC_Time.APRIL && month < TFC_Time.SEPTEMBER) {
                    CropWorldGen gen = new CropWorldGen();
                    gen.generateCrops(event.world, event.world.rand, event.getChunk().xPosition, event.getChunk().zPosition, true);

                    cropChunkData.lastSpringGenYear = TFC_Time.getYear();

                    event.getChunk().isModified = true;
                }
            }
        }
    }

}
