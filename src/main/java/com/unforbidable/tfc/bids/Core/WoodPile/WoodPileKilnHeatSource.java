package com.unforbidable.tfc.bids.Core.WoodPile;

import com.unforbidable.tfc.bids.TileEntities.TileEntityWoodPile;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.world.World;

public class WoodPileKilnHeatSource implements IKilnHeatSource {

    private final TileEntityWoodPile tileEntity;

    public WoodPileKilnHeatSource(TileEntityWoodPile tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public World getWorld() {
        return tileEntity.getWorldObj();
    }

    @Override
    public int getTileX() {
        return tileEntity.xCoord;
    }

    @Override
    public int getTileY() {
        return tileEntity.yCoord;
    }

    @Override
    public int getTileZ() {
        return tileEntity.zCoord;
    }

    @Override
    public boolean isActive() {
        return tileEntity.isBurning();
    }

    @Override
    public double getProgress() {
        return tileEntity.getKilnProgress();
    }

    @Override
    public void resetProgress() {
        tileEntity.resetKilnProgress();
    }

}
