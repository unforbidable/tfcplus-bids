package com.unforbidable.tfc.bids.Core.Kilns;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Chimney.ChimneyHelper;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.api.BidsEventFactory;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public abstract class KilnChamber<TValidator extends KilnValidator<TParams>, TParams extends KilnValidationParams> implements IKilnChamber {

    protected final IKilnHeatSource heatSource;

    private TValidator validator;
    private boolean valid;
    private TParams params;

    public KilnChamber(IKilnHeatSource heatSource) {
        this.heatSource = heatSource;
    }

    protected TValidator getValidator() {
        if (validator == null) {
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
        try {
            params = getValidator().validate();

            if (!valid) {
                Bids.LOG.debug("{}: Validated with params {}", getName(), params);
            }

            valid = true;
        } catch (KilnValidationException e) {
            // Keep the previous validation params
            // whether valid or not
            // This is needed to get to the chimney that isn't right above the heat source
            valid = false;

            Bids.LOG.debug("{}: {}", getName(), e.getMessage());
        }

        return valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public TileEntity getChimney() {
        if (params != null) {
            BlockCoord bc = getValidator().getChimneyLocation(params);
            if (bc != null) {
                BlockCoord bc2 = bc.offset(heatSource.getTileX(), heatSource.getTileY(), heatSource.getTileZ());
                TileEntity te = heatSource.getWorld().getTileEntity(bc2.x, bc2.y, bc2.z);
                if (ChimneyHelper.isChimney(te)) {
                    return te;
                } else {
                    Bids.LOG.warn("{}: Expected chimney at {},{},{}", getName(), bc.x, bc.y, bc.z);
                }
            }
        }

        return null;
    }

    @Override
    public List<BlockCoord> getPotteryBlocks() {
        ArrayList<BlockCoord> list = new ArrayList<BlockCoord>();

        if (params != null) {
            for (BlockCoord bc : getValidator().getPotteryLocations(params)) {
                BlockCoord bc2 = bc.offset(heatSource.getTileX(), heatSource.getTileY(), heatSource.getTileZ());
                // Skip air blocks
                if (!heatSource.getWorld().isAirBlock(bc2.x, bc2.y, bc2.z)) {
                    if (BidsEventFactory.onKilnFireBlockCheck(heatSource.getWorld(), bc2.x, bc2.y, bc2.z)) {
                        list.add(bc2);
                    } else {
                        Bids.LOG.warn("{}: Expected pottery at {},{},{}", getName(), bc.x, bc.y, bc.z);
                    }
                }
            }
        }

        return list;
    }

}
