package com.unforbidable.tfc.bids.features.device.woodpile.main.kiln;

import com.unforbidable.tfc.bids.features.device.woodpile.tileentity.TileEntityWoodpile;
import com.unforbidable.tfc.bids.api.features.kiln.KilnHeatSource;
import net.minecraft.world.World;

public class WoodpileKilnHeatSource implements KilnHeatSource {

    private final TileEntityWoodpile tileEntity;

    public WoodpileKilnHeatSource(TileEntityWoodpile tileEntity) {
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
