package com.unforbidable.tfc.bids.Core.Drying.Environment;

import com.dunk.tfc.Blocks.BlockRoof;
import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.WorldGen.TFCBiome;
import com.dunk.tfc.api.Interfaces.IHeatSource;
import com.dunk.tfc.api.Interfaces.IHeatSourceTE;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.util.ForgeDirection;

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

    public static boolean isExposed(World world, int blockX, int blockY, int blockZ) {
        return TFC_Core.isExposed(world, blockX, blockY, blockZ);
    }

    public static float getSunlight(World world, int blockX, int blockY, int blockZ, long ticks) {
        // Adapted from TFC_Time.getTotalHours() for specific ticks with float precision
        float totalHours = ticks / (float)TFC_Time.HOUR_LENGTH;
        float hour = (totalHours + 6) % 24;
        float hoursOffNoon = hour > 11 ? Math.max(hour - 12, 0) : 12 - hour;

        // Adapted from TFC_Time.getTotalDays() for specific ticks
        int totalDays = (int)Math.floor(ticks / 24000.0F);
        float hoursBetweenNoonAndSunset = 4 + TFC_Time.getPercentSummer(totalDays, blockZ) * 2;

        return Math.max(1 - hoursOffNoon / hoursBetweenNoonAndSunset * hoursOffNoon / hoursBetweenNoonAndSunset, 0);
    }

    private static final int AIRFLOW_MAX_VALUE = 10;
    private static final int AIRFLOW_EVALUATED_DISTANCE = 4;

    public static float getAirflow(World world, int blockX, int blockY, int blockZ) {
        if (!isBlockAllowingAirflow(world, blockX, blockY + 1, blockZ, ForgeDirection.DOWN)) {
            return 0f;
        }

        // For each direction
        // Look for up to 3 blocks of air
        // add up the factorial of the distance of the furthest block of air
        // up to max total
        int result = 0;
        for (ForgeDirection dir : new ForgeDirection[]{ForgeDirection.WEST, ForgeDirection.EAST, ForgeDirection.NORTH, ForgeDirection.SOUTH}) {
            for (int dist = 1; dist <= AIRFLOW_EVALUATED_DISTANCE; dist++) {
                if (!isBlockAllowingAirflow(world, blockX + dir.offsetX * dist, blockY, blockZ + dir.offsetZ * dist, dir.getOpposite())) {
                    break;
                }

                result = Math.min(result + dist, AIRFLOW_MAX_VALUE);
                if (result == AIRFLOW_MAX_VALUE) {
                    return 1f;
                }
            }
        }

        return result / (float)AIRFLOW_MAX_VALUE;
    }

    private static boolean isBlockAllowingAirflow(World world, int x, int y, int z, ForgeDirection side) {
        Block block = world.getBlock(x, y, z);
        return block.getMaterial() == Material.air ||
            !(block instanceof BlockRoof) &&
            !block.renderAsNormalBlock() &&
            !block.isOpaqueCube() &&
            !block.isSideSolid(world, x, y, z, side);
    }

}
