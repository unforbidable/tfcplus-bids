package com.unforbidable.tfc.bids.features.resource.flora.eventhandler;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.chunk.ChunkData;
import com.unforbidable.tfc.bids.features.resource.flora.main.FernChunkData;
import com.unforbidable.tfc.bids.features.resource.flora.main.FernGrowthHelper;
import com.unforbidable.tfc.bids.util.BlockCoord;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraftforge.event.world.ChunkEvent;

public class FernChunkEventHandler {

    @SubscribeEvent
    public void onLoad(ChunkEvent.Load event) {
        if (!event.world.isRemote) {
            FernChunkData cropChunkData = ChunkData.of(event.getChunk()).get(FernChunkData.class);
            if (cropChunkData.nextGrowthDay != 0 && cropChunkData.nextGrowthDay <= TFC_Time.getTotalDays()) {
                Set<BlockCoord> area = FernGrowthHelper.getChunkFernSpreadArea(event.world, event.getChunk().xPosition, event.getChunk().zPosition);
                if (area != null) {
                    for (BlockCoord bc : area) {
                        Block block = event.world.getBlock(bc.x, bc.y, bc.z);
                        if (block == BidsBlocks.brackenFern) {
                            // Force update to check growth
                            event.world.scheduleBlockUpdate(bc.x, bc.y, bc.z, block, 20);
                            break;
                        }
                    }
                }
            }
        }
    }

}
