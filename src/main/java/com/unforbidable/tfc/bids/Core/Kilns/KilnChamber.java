package com.unforbidable.tfc.bids.Core.Kilns;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class KilnChamber<TValidator extends KilnValidator<?>> implements IKilnChamber {

    protected final IKilnHeatSource heatSource;
    protected TValidator validator;

    public KilnChamber(IKilnHeatSource heatSource) {
        this.heatSource = heatSource;
    }

    protected TValidator getValidator() {
        if (validator == null) {
            Bids.LOG.warn("Validator was accessed prior to validation");
            Thread.dumpStack();

            validator = createValidator();
        }

        return validator;
    }

    private TValidator createValidator() {
        return createValidator(heatSource.getWorld(), heatSource.getTileX(), heatSource.getTileY(), heatSource.getTileZ());
    }

    protected abstract TValidator createValidator(World world, int x, int y, int z);

    @Override
    public boolean validate() {
        TValidator temp = createValidator();
        if (temp.isValid()) {
            validator = temp;
            return true;
        } else {
            // Keep the previous validator
            // so that the previous validation result is available
            // This is needed to get to the chimney that isn't right above the heat source
            return false;
        }
    }

    @Override
    public boolean isValid() {
        return getValidator().isValid();
    }

    @Override
    public List<TileEntity> getPottery() {
        List<TileEntity> list = new ArrayList<TileEntity>();

        List<BlockCoord> potteryLocations = getPotteryLocations();
        if (potteryLocations != null) {
            for (BlockCoord bc : potteryLocations) {
                TileEntity te = heatSource.getWorld().getTileEntity(bc.x, bc.y, bc.z);
                if (te != null) {
                    if (KilnHelper.isPottery(te)) {
                        list.add(te);
                    } else {
                        Bids.LOG.warn("Expected pottery at {},{},{}", bc.x, bc.y, bc.z);
                    }
                }
            }
        }

        return list;
    }

    @Override
    public TileEntity getChimney() {
        BlockCoord chimneyLocation = getChimneyLocation();
        if (chimneyLocation != null) {
            TileEntity te = heatSource.getWorld().getTileEntity(chimneyLocation.x, chimneyLocation.y, chimneyLocation.z);
            if (ChimneyHelper.isChimney(te)) {
                return te;
            } else {
                Bids.LOG.warn("Expected chimney at {},{},{}", chimneyLocation.x, chimneyLocation.y, chimneyLocation.z);
            }
        }

        return null;
    }

    protected BlockCoord getChimneyLocation() {
        return null;
    }

    protected List<BlockCoord> getPotteryLocations() {
        return null;
    }

}
