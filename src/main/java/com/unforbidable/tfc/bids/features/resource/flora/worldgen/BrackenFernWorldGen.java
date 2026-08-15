package com.unforbidable.tfc.bids.features.resource.flora.worldgen;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.core.chunk.ChunkData;
import com.unforbidable.tfc.bids.features.resource.flora.main.FernChunkData;
import com.unforbidable.tfc.bids.features.resource.flora.main.FernGrowthHelper;
import com.unforbidable.tfc.bids.util.BlockCoord;
import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import java.util.Set;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class BrackenFernWorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider iChunkProvider, IChunkProvider iChunkProvider1) {
        Set<BlockCoord> area = FernGrowthHelper.getChunkFernSpreadArea(world, chunkX, chunkZ);
        if (area != null) {
            FernChunkData fernChunkData = ChunkData.of(world, chunkX, chunkZ).get(FernChunkData.class);
            fernChunkData.nextGrowthDay = TFC_Time.getTotalDays() + FernGrowthHelper.getChunkGrowthDaysNeeded(world, chunkX, chunkZ, TFC_Time.getTotalDays());

            int goodY = FernGrowthHelper.getFirstCanGrowFernHeight(world, area);
            if (goodY > 0) {
                for (BlockCoord bc : area) {
                    if (FernGrowthHelper.checkHeight(goodY, bc.y) && FernGrowthHelper.canGrowFern(world, bc.x, bc.y, bc.z)) {
                        FernGrowthHelper.growFern(world, bc.x, bc.y, bc.z);
                    }
                }
            }
        }
    }

}
