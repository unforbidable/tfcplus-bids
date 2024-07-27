package com.unforbidable.tfc.bids.Core.Kilns;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public abstract class KilnValidator<TResultParams extends KilnValidationParams> {

    protected static final ForgeDirection[] HORIZONTAL_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    protected final World world;
    protected final int sourceX;
    protected final int sourceY;
    protected final int sourceZ;

    KilnValidatorResult<TResultParams> result;

    public KilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        this.world = world;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
    }

    public boolean isValid() {
        return getResult().valid;
    }

    public KilnValidatorResult<TResultParams> getResult() {
        if (result == null) {
            TResultParams params = validateStructure();
            result = new KilnValidatorResult<TResultParams>(params != null, params);
        }

        return result;
    }

    public final BlockCoord getChimneyLocation() {
        if (getResult().valid) {
            return getChimneyLocation(getResult().params);
        } else {
            return null;
        }
    }

    public final List<BlockCoord> getPotteryLocations() {
        if (getResult().valid) {
            return getPotteryLocations(getResult().params);
        } else {
            return new ArrayList<BlockCoord>();
        }
    }

    public BlockCoord getChimneyLocation(TResultParams params) {
        return null;
    }

    public List<BlockCoord> getPotteryLocations(TResultParams params) {
        return new ArrayList<BlockCoord>();
    }

    protected abstract TResultParams validateStructure();

    protected boolean checkChimneyTier(int x, int y, int z, int tier) {
        return KilnValidationHelper.isChimneyTier(world, sourceX + x, sourceY + y, sourceZ + z, tier);
    }

    protected boolean requireAir(int x, int y, int z) {
        boolean success = KilnValidationHelper.isAir(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            Bids.LOG.info("Expected air at {},{},{}", x, y, z);
        }
        return success;
    }

    protected boolean requireAirOrPottery(int x, int y, int z) {
        boolean success = KilnValidationHelper.isAirOrPottery(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            Bids.LOG.info("Expected air or pottery at {},{},{}", x, y, z);
        }
        return success;
    }

    protected boolean checkWall(int x, int y, int z, ForgeDirection d) {
        return KilnValidationHelper.isWall(world, sourceX + x, sourceY + y, sourceZ + z, d);
    }

    protected boolean requireWall(int x, int y, int z, ForgeDirection d) {
        boolean success = KilnValidationHelper.isWall(world, sourceX + x, sourceY + y, sourceZ + z, d);
        if (!success) {
            Bids.LOG.info("Expected wall {} at {},{},{}", d, x, y, z);
        }
        return success;
    }

    protected boolean requireAirOrFire(int x, int y, int z) {
        boolean success = KilnValidationHelper.isAirOrFire(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            Bids.LOG.info("Expected air or fire at {},{},{}", x, y, z);
        }
        return success;
    }

    protected boolean requireFireBrick(int x, int y, int z) {
        boolean success = KilnValidationHelper.isFireBrick(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            Bids.LOG.info("Expected fire brick at {},{},{}", x, y, z);
        }
        return success;
    }

}
