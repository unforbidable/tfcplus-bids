package com.unforbidable.tfc.bids.Core.Kilns;

import com.unforbidable.tfc.bids.Core.Common.BlockCoord;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public abstract class KilnValidator<TParams extends KilnValidationParams> {

    protected static final ForgeDirection[] HORIZONTAL_DIRECTIONS = new ForgeDirection[] { ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.WEST, ForgeDirection.EAST };

    private final World world;
    private final int sourceX;
    private final int sourceY;
    private final int sourceZ;

    public KilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        this.world = world;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
    }

    public BlockCoord getChimneyLocation(TParams params) {
        return null;
    }

    public List<BlockCoord> getPotteryLocations(TParams params) {
        return new ArrayList<BlockCoord>();
    }

    public abstract TParams validate() throws KilnValidationException;

    protected boolean checkChimneyTier(int x, int y, int z, int tier) {
        return KilnValidationHelper.isChimneyTier(world, sourceX + x, sourceY + y, sourceZ + z, tier);
    }

    protected void requireChimneyTier(int x, int y, int z, int tier) throws KilnValidationException {
        boolean success = KilnValidationHelper.isChimneyTier(world, sourceX + x, sourceY + y, sourceZ + z, tier);
        if (!success) {
            throw new KilnValidationException(String.format("Expected chimney tier %d or higher at %d,%d,%d", tier, x, y, z));
        }
    }

    protected void requireAir(int x, int y, int z) throws KilnValidationException {
        boolean success = KilnValidationHelper.isAir(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            throw new KilnValidationException(String.format("Expected air at %d,%d,%d", x, y, z));
        }
    }

    protected void requireAirOrPottery(int x, int y, int z) throws KilnValidationException {
        boolean success = KilnValidationHelper.isAirOrPottery(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            throw new KilnValidationException(String.format("Expected air or pottery at %d,%d,%d", x, y, z));
        }
    }

    protected boolean checkWall(int x, int y, int z, ForgeDirection d) {
        return KilnValidationHelper.isWall(world, sourceX + x, sourceY + y, sourceZ + z, d);
    }

    protected void requireWall(int x, int y, int z, ForgeDirection d) throws KilnValidationException {
        boolean success = KilnValidationHelper.isWall(world, sourceX + x, sourceY + y, sourceZ + z, d);
        if (!success) {
            throw new KilnValidationException(String.format("Expected wall %s at %d,%d,%d", d, x, y, z));
        }
    }

    protected void requireAirOrFire(int x, int y, int z) throws KilnValidationException {
        boolean success = KilnValidationHelper.isAirOrFire(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            throw new KilnValidationException(String.format("Expected air or fire at %d,%d,%d", x, y, z));
        }
    }

    protected void requireFireBrick(int x, int y, int z) throws KilnValidationException {
        boolean success = KilnValidationHelper.isFireBrick(world, sourceX + x, sourceY + y, sourceZ + z);
        if (!success) {
            throw new KilnValidationException(String.format("Expected fire brick at %d,%d,%d", x, y, z));
        }
    }

}
