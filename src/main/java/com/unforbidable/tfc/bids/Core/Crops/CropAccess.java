package com.unforbidable.tfc.bids.Core.Crops;

import com.unforbidable.tfc.bids.TileEntities.TileEntityNewCrop;
import com.unforbidable.tfc.bids.TileEntities.TileEntityNewFarmland;
import net.minecraft.world.IBlockAccess;

public class CropAccess {

    private final IBlockAccess world;
    private final int x;
    private final int y;
    private final int z;

    private TileEntityNewCrop tileEntityNewCrop;
    private TileEntityNewFarmland tileEntityNewFarmland;
    private BidsCropIndex cropIndex;

    public CropAccess(IBlockAccess world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public CropAccess(TileEntityNewCrop tileEntityNewCrop) {
        this.world = tileEntityNewCrop.getWorldObj();
        this.x = tileEntityNewCrop.xCoord;
        this.y = tileEntityNewCrop.yCoord;
        this.z = tileEntityNewCrop.zCoord;

        this.tileEntityNewCrop = tileEntityNewCrop;
    }

    public TileEntityNewCrop getCropTileEntity() {
        if (tileEntityNewCrop == null) {
            tileEntityNewCrop = (TileEntityNewCrop) world.getTileEntity(x, y, z);
        }

        return tileEntityNewCrop;
    }

    public TileEntityNewFarmland getFarmlandTileEntity() {
        if (tileEntityNewFarmland == null && world.getTileEntity(x, y - 1, z) instanceof TileEntityNewFarmland) {
            tileEntityNewFarmland = (TileEntityNewFarmland) world.getTileEntity(x, y - 1, z);
        }

        return tileEntityNewFarmland;
    }

    public boolean hasFarmland() {
        return world.getTileEntity(x, y - 1, z) instanceof TileEntityNewFarmland;
    }

    public BidsCropIndex getIndex() {
        if (cropIndex == null) {
            cropIndex = BidsCropManager.findCropById(getCropTileEntity().cropId);
        }

        return cropIndex;
    }

    public int getGrowthStage() {
        return Math.min((int) Math.floor(getCropTileEntity().growth), getIndex().numGrowthStages);
    }

    public float getGrowth() {
        return getCropTileEntity().growth;
    }

    public float getNutrientMultiplier() {
        if (hasFarmland()) {
            return Math.min(getCropTileEntity().nutrients / (getIndex().growthTime * 92.4f), 1f);
        } else {
            return 0.6f;
        }
    }

    public float getLastStageGrowthMultiplier() {
        return getGrowth() < getIndex().numGrowthStages ? (getGrowth() - getIndex().numGrowthStages + 1) * 0.5f + 0.1f : 1f;
    }

}
