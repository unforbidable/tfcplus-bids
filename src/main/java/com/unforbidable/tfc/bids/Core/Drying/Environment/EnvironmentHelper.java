package com.unforbidable.tfc.bids.Core.Drying.Environment;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.WorldGen.TFCBiome;
import com.dunk.tfc.api.Interfaces.IHeatSource;
import com.dunk.tfc.api.Interfaces.IHeatSourceTE;
import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class EnvironmentHelper {

    public static boolean isHeatSourceNearby(World world, int blockX, int blockY, int blockZ) {
        // Adapted from TFC_Core.shouldUpdateLocalWarmthFromHeatSource()

        int radius = 7;
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                for (int k = -radius; k < radius; k++) {
                    float distance = (float) i * i + j * j + k * k;
                    Block b = world.getBlock(blockX + i, blockY + j, blockZ + k);
                    if (b instanceof IHeatSource && distance <= ((IHeatSource) b).getHeatSourceRadius() * ((IHeatSource) b).getHeatSourceRadius()
                        && ((IHeatSource) b).getTileEntityType() != null) {
                        IHeatSourceTE te = (IHeatSourceTE) world.getTileEntity(blockX + i, blockY + j, blockZ + k);
                        if (te.getHeatSourceTemp() > 40) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public static float getTemperature(World world, int blockX, int blockY, int blockZ, long ticks) {
        int th = (int)(ticks / TFC_Time.HOUR_LENGTH);
        int day = TFC_Time.getDayFromTotalHours(th);
        int hour = TFC_Time.getHourOfDayFromTotalHours(th);
        return TFC_Climate.getHeightAdjustedTempSpecificDay(world, day, hour, blockX, blockY, blockZ);
    }

    public static float getPrecipitation(World world, int blockX, int blockY, int blockZ, long ticks) {
        // Estimate rain strength based on rainfall
        float rainfall = TFC_Climate.getRainfall(world, blockX, blockY, blockZ);
        float rainStrength = Math.min(rainfall * rainfall / 4000000, 1);
        long elapsedTicks = TFC_Time.getTotalTicks() - ticks;
        if (elapsedTicks < 100) {
            return world.isRaining() ? rainStrength : 0f;
        } else {
            // There is no way to know whether it rained or not at any moment in the past
            // but assume if it is raining now, it has been raining for a while
            // but why not consider the rainfall value
            // Hoping with upcoming rain changes we can track past rain with sufficient precision
            return world.isRaining() && elapsedTicks < rainfall * 2 ? rainStrength : 0;
        }
    }

    public static float getHumidity(World world, int blockX, int blockY, int blockZ, float temperature, float precipitation) {
        // Adapted from TFC_Core.getHumidity()
        // provided temperature and rain is considered

        float rainfall = TFC_Climate.getRainfall(world, blockX, blockY, blockZ);
        float tempScale = temperature < 0.0F ? 0.0F : 1.0F;
        float result = 0.0F;
        float swampy = 0.0F;

        for(int i = -3; i <= 2 && rainfall < 2750.0F; ++i) {
            for(int j = -3; j <= 2; ++j) {
                BiomeGenBase biome = world.getBiomeGenForCoords(blockX + i, blockZ + j);
                if (biome == TFCBiome.SWAMPLAND || biome == TFCBiome.SALTSWAMP) {
                    ++swampy;
                }
            }
        }

        if (rainfall <= 2750.0F && swampy / 25.0F > 0.0F) {
            rainfall += 1000.0F * (swampy / 25.0F);
        }

        if (rainfall > 250.0F) {
            result = Math.min(rainfall - 250.0F, 2500.0F) / 2500.0F;
        }

        boolean isRaining = precipitation > 0;
        return tempScale * Math.max(result, isRaining ? 1.0F : 0.0F);
    }

    public static Boolean isExposed(World world, int blockX, int blockY, int blockZ) {
        return TFC_Core.isExposed(world, blockX, blockY, blockZ);
    }

}
