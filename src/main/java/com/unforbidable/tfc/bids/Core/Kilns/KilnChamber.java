package com.unforbidable.tfc.bids.Core.Kilns;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnChamber;
import com.unforbidable.tfc.bids.api.Interfaces.IKilnHeatSource;
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
        TParams temp = getValidator().validate();
        if (temp != null) {
            valid = true;
            params = temp;
        } else {
            // Keep the previous validation params
            // whether valid or not
            // This is needed to get to the chimney that isn't right above the heat source
            valid = false;
        }

        return valid;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public BlockCoord getChimneyLocation() {
        if (params != null) {
            return getValidator().getChimneyLocation(params);
        } else {
            return null;
        }
    }

    @Override
    public List<BlockCoord> getPotteryLocations() {
        if (params != null) {
            return getValidator().getPotteryLocations(params);
        } else {
            return new ArrayList<BlockCoord>();
        }
    }

}
