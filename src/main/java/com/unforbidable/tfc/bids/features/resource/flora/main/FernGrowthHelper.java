package com.unforbidable.tfc.bids.features.resource.flora.main;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.WorldGen.TFCBiome;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.core.chunk.ChunkData;
import com.unforbidable.tfc.bids.features.resource.flora.FloraConfig;
import com.unforbidable.tfc.bids.util.BlockCoord;
import com.unforbidable.tfc.bids.util.WorldGenHelper;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.util.ForgeDirection;

public class FernGrowthHelper {

    public static final float MIN_GROW_RAINFALL = 735;
    public static final float MAX_GROW_RAINFALL = 2735;

    public static final float MIN_SPREAD_TEMP = 10;
    public static final float MAX_SPREAD_TEMP = 30;

    public static final int BASE_GROWTH_DAYS = 4;

    public static boolean tryToSpreadFern(World world, int x, int y, int z, Random random) {
        Chunk chunk = world.getChunkFromBlockCoords(x, z);
        FernChunkData data = ChunkData.of(chunk).get(FernChunkData.class);

        // next growth day depends on rain
        int next = getChunkGrowthDaysNeeded(world, chunk.xPosition, chunk.zPosition, data.nextGrowthDay);

        if (data.nextGrowthDay == 0) {
            data.nextGrowthDay = TFC_Time.getTotalDays() + next;

            Bids.LOG.debug("Fern managed to spread to a new chunk [{},{},{}]", x, y, z);

        } else if (data.nextGrowthDay <= TFC_Time.getTotalDays()) {
            data.nextGrowthDay += next;
            chunk.isModified = true;

            // Enough time has passed and the plant can spread
            // if temperature is good
            // at the time of supposed growth when catching up
            float temp = TFC_Climate.getHeightAdjustedTempSpecificDay(world, data.nextGrowthDay, x, y, z);

            if (temp >= MIN_SPREAD_TEMP && temp <= MAX_SPREAD_TEMP) {
                Set<BlockCoord> area = getChunkFernSpreadArea(world, chunk.xPosition, chunk.zPosition);

                if (area != null) {
                    // If inside an original fern chunk
                    // try to grow back any harvested block

                    int goodY = getFirstCanGrowFernHeight(world, area);
                    if (goodY > 0) {
                        for (BlockCoord bc : area) {
                            if (checkHeight(goodY, bc.y) && canGrowFern(world, bc.x, bc.y, bc.z)) {
                                growFern(world, bc.x, bc.y, bc.z);

                                Bids.LOG.debug("Fern grew back at [{},{},{}]", bc.x, bc.y, bc.z);

                                // More can grow?
                                return data.nextGrowthDay <= TFC_Time.getTotalDays();
                            }
                        }
                    }
                }

                // When outside an original fern chunk
                // or if fully grown back
                // try to spread more but very slowly
                if (random.nextInt(4) == 0) {
                    int n = random.nextInt(4);
                    ForgeDirection dir = WorldGenHelper.YAXIS_DIRS[n];
                    int x2 = x + dir.offsetX;
                    int z2 = z + dir.offsetZ;
                    int y2 = world.getTopSolidOrLiquidBlock(x2, z2);

                    if (checkHeight(y, y2) && canGrowFern(world, x2, y2, z2)) {
                        growFern(world, x2, y2, z2);

                        Bids.LOG.debug("Fern expanded at [{},{},{}]", x2, y2, z2);
                    }
                }

                return data.nextGrowthDay <= TFC_Time.getTotalDays();
            }
        }

        return false;
    }

    public static int getFirstCanGrowFernHeight(World world, Set<BlockCoord> area) {
        for (BlockCoord bc : area) {
            if (FernGrowthHelper.canGrowFern(world, bc.x, bc.y, bc.z)) {
                return bc.y;
            }
        }

        return 0;
    }

    public static boolean checkHeight(int sourceY, int targetY) {
        return Math.abs(sourceY - targetY) < 4;
    }

    public static boolean canGrowFern(World world, int x, int y, int z) {
        return BidsBlocks.brackenFern.canBlockStay(world, x, y, z) &&
           (world.isAirBlock(x, y, z) || world.getBlock(x, y, z) == TFCBlocks.tallGrass || world.getBlock(x, y, z) == TFCBlocks.leafLitter);
    }

    public static void growFern(World world, int x, int y, int z) {
        world.setBlock(x, y, z, BidsBlocks.brackenFern);
    }

    public static int getChunkGrowthDaysNeeded(World world, int chunkX, int chunkZ, int totalDays) {
        int x = (chunkX << 4) + 8;
        int z = (chunkZ << 4) + 8;

        float rain = TFC_Climate.getRainfall(world, x, Global.SEALEVEL, z);
        return Math.round(Math.max(BASE_GROWTH_DAYS, Math.round(12000 / rain)) * TFC_Time.getYearRatio(96));
    }

    public static boolean isValidFernBiomeAt(World world, int x, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        return biome != TFCBiome.LAKE && biome != TFCBiome.RIVER &&
            biome != TFCBiome.LAKESHORE && biome != TFCBiome.RIVERBANK &&
            biome != TFCBiome.BEACH && biome != TFCBiome.GRAVEL_BEACH && biome != TFCBiome.SHORE &&
            biome != TFCBiome.OCEAN && biome != TFCBiome.DEEP_OCEAN && biome != TFCBiome.ESTUARY &&
            biome != TFCBiome.SWAMPLAND && biome != TFCBiome.SALTSWAMP;
    }

    public static Set<BlockCoord> getChunkFernSpreadArea(World world, int chunkX, int chunkZ) {
        Random random = WorldGenHelper.getChunkYearRandom(world, chunkX, chunkZ, 0);
        int chunkCheck = random.nextInt(256);

        if (chunkCheck < 8 * FloraConfig.fernChanceMultiplier) {
            int xCoord = (chunkX << 4) + random.nextInt(8) + 4;
            int zCoord = (chunkZ << 4) + random.nextInt(8) + 4;

            if (isValidFernBiomeAt(world, xCoord, zCoord)) {
                float rain = TFC_Climate.getRainfall(world, xCoord, Global.SEALEVEL, zCoord);

                if (rain > MIN_GROW_RAINFALL && rain < MAX_GROW_RAINFALL) {
                    int spread = Math.min(Math.round(10 * FloraConfig.fernBaseSizeMultiplier), Math.round(rain / 50)) + random.nextInt(5);

                    Set<BlockCoord> area = WorldGenHelper.getClusterArea(random, xCoord, Global.SEALEVEL, zCoord, spread);

                    Set<BlockCoord> surface = new HashSet<>();
                    for (BlockCoord bc : area) {
                        int y = world.getTopSolidOrLiquidBlock(bc.x, bc.z);
                        surface.add(new BlockCoord(bc.x, y, bc.z));
                    }

                    return surface;
                }
            }
        }

        return null;
    }

}
