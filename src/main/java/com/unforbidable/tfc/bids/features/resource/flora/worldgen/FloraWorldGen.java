package com.unforbidable.tfc.bids.features.resource.flora.worldgen;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.WorldGen.TFCBiome;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.util.BlockCoord;
import com.unforbidable.tfc.bids.util.WorldGenHelper;
import cpw.mods.fml.common.IWorldGenerator;
import java.util.Random;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fluids.IFluidBlock;

public class FloraWorldGen implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        generateNettleChunk(world, random, chunkX, chunkZ, false);
    }

    public void generateNettleChunk(World world, Random random, int chunkX, int chunkZ, boolean springGen) {
        int chunkXCoord = chunkX << 4;
        int chunkZCoord = chunkZ << 4;

        float temp = TFC_Climate.getBioTemperatureHeight(world, chunkXCoord, Global.SEALEVEL, chunkZCoord);
        if (temp > -13 && temp < 17) {
            float rain = TFC_Climate.getRainfall(world, chunkXCoord, Global.SEALEVEL, chunkZCoord);

            int attempts = 20;
            int spread = 4 + Math.min(Math.round(rain / 400), 4) + random.nextInt(4);

            for (int i = 0; i < attempts; i++) {
                int x = chunkXCoord + random.nextInt(16) + 8;
                int z = chunkZCoord + random.nextInt(16) + 8;
                int y = world.getTopSolidOrLiquidBlock(x, z);
                if (y == Global.SEALEVEL && isBlockNextToFreshWater(world, x, y, z) && canNettleGrowBlock(world, x, y, z)) {
                    boolean isBiomeGood = isFreshWaterBiomeAt(world, x, z);
                    int spreadAdjustedForBiome = isBiomeGood ? spread : spread / 2;
                    Set<BlockCoord> cluster = WorldGenHelper.getClusterArea(random, x, y, z, spreadAdjustedForBiome);
                    if (isBiomeGood && !springGen) {
                        Set<BlockCoord> border = WorldGenHelper.getBorderOfArea(cluster);
                        for (BlockCoord bc : border) {
                            if (isBlockNextToFreshWater(world, bc.x, bc.y, bc.z)) {
                                Set<BlockCoord> extension = WorldGenHelper.getClusterArea(random, bc.x, bc.y, bc.z, spreadAdjustedForBiome / 2);
                                cluster.addAll(extension);
                            }
                        }
                    }
                    for (BlockCoord bc : cluster) {
                        int cx = x >> 4;
                        int cz = z >> 4;
                        if (world.getChunkProvider() != null && world.getChunkProvider().chunkExists(cx, cz)) {
                            for (int j = 0; j < 3; j++) {
                                if (canNettleGrowBlock(world, bc.x, bc.y + j, bc.z)) {
                                    growNettle(world, bc.x, bc.y + j, bc.z);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean isBlockNextToFreshWater(World par1World, int x, int y, int z) {
        return isFreshWaterBlock(par1World.getBlock(x - 1, y - 1, z)) ||
            isFreshWaterBlock(par1World.getBlock(x + 1, y - 1, z)) ||
            isFreshWaterBlock(par1World.getBlock(x, y - 1, z - 1)) ||
            isFreshWaterBlock(par1World.getBlock(x, y - 1, z + 1));
    }

    private static boolean isFreshWaterBlock(Block block) {
        return (block instanceof IFluidBlock) && ((IFluidBlock) block).getFluid() == TFCFluids.FRESHWATER;
    }

    private boolean isFreshWaterBiomeAt(World world, int x, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        return biome == TFCBiome.RIVER || biome == TFCBiome.RIVERBANK || biome == TFCBiome.LAKE || biome == TFCBiome.LAKESHORE || biome == TFCBiome.SWAMPLAND;
    }

    private boolean canNettleGrowBlock(World world, int x, int y, int z) {
        return BidsBlocks.moreGrass.canBlockStay(world, x, y, z)
            && (world.isAirBlock(x, y, z) || world.getBlock(x, y, z) == TFCBlocks.tallGrass);
    }

    private void growNettle(World world, int x, int y, int z) {
        world.setBlock(x, y, z, BidsBlocks.moreGrass);
    }

}
