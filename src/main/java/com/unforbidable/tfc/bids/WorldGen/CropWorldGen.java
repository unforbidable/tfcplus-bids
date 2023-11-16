package com.unforbidable.tfc.bids.WorldGen;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.WorldGen.TFCBiome;
import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropIndex;
import com.unforbidable.tfc.bids.Core.Crops.CropHelper;
import com.unforbidable.tfc.bids.Core.Crops.BidsCropManager;
import com.unforbidable.tfc.bids.Core.WorldGen.WorldGenHelper;
import cpw.mods.fml.common.IWorldGenerator;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;
import java.util.Random;

public class CropWorldGen implements IWorldGenerator {

    // 10 is about how many crops TFC generates per region
    // so the same number is used here even though there is fewer crops generated
    // the chance of a single crop to generate will be similar to what it is for TFC crops
    private static final int MIN_CROP_SLOTS = 10;

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        generateCrops(world, random, chunkX, chunkZ, false);
    }

    public void generateCrops(World world, Random random, int chunkX, int chunkZ, boolean springGen) {
        int chunkXCoord = chunkX << 4;
        int chunkZCoord = chunkZ << 4;
        int region = TFC_Climate.getRegionLayer(world, chunkXCoord, Global.SEALEVEL, chunkZCoord);
        boolean coast = isCoast(world, chunkXCoord, chunkZCoord);
        List<BidsCropIndex> validCrops = BidsCropManager.getValidCrops(region, coast);
        if (validCrops.size() > 0) {
            Random randCropType = WorldGenHelper.getChunkYearRandom(world, chunkX >> 3, chunkZ >> 3, 0);
            int index = randCropType.nextInt(Math.max(validCrops.size(), MIN_CROP_SLOTS));
            if (index < validCrops.size()) {
                Random randCropGen = WorldGenHelper.getChunkYearRandom(world, chunkX, chunkZ, TFC_Time.getYear());
                int chance = coast ? 8 : 16;
                if (randCropGen.nextInt(chance) == 0) {
                    BidsCropIndex crop = validCrops.get(index);
                    generateCrop(crop, world, randCropGen, chunkX, chunkZ, springGen, coast);
                }
            }
        }
    }

    private boolean isCoast(World world, int xCoord, int zCoord) {
        for (int i = -3; i < 4; i++) {
            for (int j = -3; j < 4; j++) {
                int x = xCoord + i * 2;
                int z = zCoord + j * 2;
                if (isCoastalBiomeAt(world, x, z)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isCoastalBiomeAt(World world, int x, int z) {
        BiomeGenBase biome = world.getBiomeGenForCoords(x, z);
        return biome == TFCBiome.SHORE || biome == TFCBiome.BEACH || biome == TFCBiome.GRAVEL_BEACH || biome == TFCBiome.SALTSWAMP;
    }

    private void generateCrop(BidsCropIndex cropIndex, World world, Random rand, int chunkX, int chunkZ, boolean springGen, boolean coast) {
        if (CropHelper.canCropGrowChunk(cropIndex, world, rand, chunkX, chunkZ)) {

            int num = coast ? 4 + rand.nextInt(12) : springGen ? 1 + rand.nextInt(5) : 2 + rand.nextInt(8);
            int xCoord = chunkX * 16 + rand.nextInt(16) - 8;
            int zCoord = chunkZ * 16 + rand.nextInt(16) - 8;

            for (int i = 0; i < num; i++) {
                int x = xCoord + rand.nextInt(16) - 8;
                int z = zCoord + rand.nextInt(16) - 8;
                int y = world.getTopSolidOrLiquidBlock(x, z);
                if (CropHelper.canCropGrowBlock(cropIndex, world, x, y, z)) {
                    CropHelper.growCrop(cropIndex, world, rand, x, y, z);
                }
            }
        }
    }

}
