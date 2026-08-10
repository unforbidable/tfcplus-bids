package com.unforbidable.tfc.bids.core.crop;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TEFarmland;
import com.dunk.tfc.api.TFCBlocks;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.api.BidsBlocks;
import com.unforbidable.tfc.bids.features.resource.crop.tileentity.TileEntityNewCrop;
import com.unforbidable.tfc.bids.features.resource.crop.tileentity.TileEntityNewFarmland;
import java.util.Random;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class CropHelper {

    public static boolean isFarmland(Block block) {
        return block == BidsBlocks.newTilledSoil || block == BidsBlocks.newTilledSoil2;
    }

    public static void placeNewFarmlandAt(World world, int x, int y, int z) {
        NBTTagCompound nbt = new NBTTagCompound();
        TEFarmland oldFarmland = (TEFarmland) world.getTileEntity(x, y, z);
        oldFarmland.writeToNBT(nbt);

        Block farmland = world.getBlock(x, y, z);
        int farmlandMetadata = world.getBlockMetadata(x, y, z);

        if (farmland == TFCBlocks.tilledSoil) {
            world.setBlock(x, y, z, BidsBlocks.newTilledSoil, farmlandMetadata, 2);
        } else {
            world.setBlock(x, y, z, BidsBlocks.newTilledSoil2, farmlandMetadata, 2);
        }

        TileEntityNewFarmland newFarmland = (TileEntityNewFarmland) world.getTileEntity(x, y, z);
        newFarmland.readFromNBT(nbt);
    }

    public static void restoreOldFarmland(World world, int x, int y, int z) {
        NBTTagCompound nbt = new NBTTagCompound();
        TileEntityNewFarmland newFarmland = (TileEntityNewFarmland) world.getTileEntity(x, y, z);
        newFarmland.writeToNBT(nbt);

        Block farmland = world.getBlock(x, y, z);
        int farmlandMetadata = world.getBlockMetadata(x, y, z);
        if (farmland == BidsBlocks.newTilledSoil) {
            world.setBlock(x, y, z, TFCBlocks.tilledSoil, farmlandMetadata, 2);
        } else {
            world.setBlock(x, y, z, TFCBlocks.tilledSoil2, farmlandMetadata, 2);
        }

        TEFarmland oldFarmland = (TEFarmland) world.getTileEntity(x, y, z);
        oldFarmland.readFromNBT(nbt);
    }


    public static boolean canCropGrowChunk(BidsCropIndex cropIndex, World world, Random rand, int chunkX, int chunkZ) {
        return true;
    }

    public static void growCrop(BidsCropIndex cropIndex, World world, Random rand, int x, int y, int z) {
        world.setBlock(x, y, z, BidsBlocks.newCrops);

        // Ensure block has been placed
        // as it can fail due to other generation, especially springs
        TileEntityNewCrop te = (TileEntityNewCrop)world.getTileEntity(x, y, z);
        if (te != null) {

            float gt = Math.max((float)cropIndex.growthTime / TFC_Time.daysInMonth, 0.01f);
            int month = TFC_Time.getSeasonAdjustedMonth(z);
            float mg = Math.min(month / gt, 1.0f) * (0.75f + (rand.nextFloat() * 0.25f));
            float growth = Math.min(cropIndex.numGrowthStages * mg, cropIndex.numGrowthStages);

            te.cropId = cropIndex.cropId;
            te.growth = growth;
        }
    }

    public static boolean canCropGrowBlock(BidsCropIndex cropIndex, World world, int x, int y, int z) {
        float temp = TFC_Climate.getHeightAdjustedTempSpecificDay(world, TFC_Time.getTotalDays(), x, y, z);
        if (temp < cropIndex.minAliveTemp) {
            return false;
        }

        float rainfall = TFC_Climate.getRainfall(world, x, y, z);
        if (rainfall < cropIndex.minimumNaturalRain || rainfall > cropIndex.maximumNaturalRain) {
            return false;
        }

        float bioTemp = TFC_Climate.getBioTemperatureHeight(world, x, y, z);
        if (bioTemp < cropIndex.minimumNaturalBioTemp || bioTemp > cropIndex.maximumNaturalBioTemp) {
            return false;
        }

        if (!canCropReplaceBlockAt(cropIndex, world, x, y, z)) {
            return false;
        }

        if (!canCropStay(cropIndex, world, x, y, z)) {
            return false;
        }

        return true;
    }

    public static boolean canCropReplaceBlockAt(BidsCropIndex cropIndex, World world, int x, int y, int z) {
        return world.isAirBlock(x, y, z) || world.getBlock(x, y, z) == TFCBlocks.tallGrass;
    }

    public static boolean canCropStay(BidsCropIndex cropIndex, World world, int x, int y, int z) {
        return BidsBlocks.newCrops.canBlockStay(world, x, y, z);
    }

}
