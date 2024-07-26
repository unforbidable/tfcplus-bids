package com.unforbidable.tfc.bids.Core.Kilns;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public abstract class KilnValidator<TResultParams> {

    private final World world;
    private final int sourceX;
    private final int sourceY;
    private final int sourceZ;

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

    public TResultParams getParams() {
        return result.params;
    }

    protected KilnValidatorResult<TResultParams> getResult() {
        if (result == null) {
            TResultParams params = validateStructure();
            result = new KilnValidatorResult<TResultParams>(params != null, params);
        }

        return result;
    }

    protected abstract TResultParams validateStructure();

    protected boolean checkChimneyTier(int x, int y, int z, int tier) {
        return KilnValidationHelper.isChimneyTier(world, sourceX + x, sourceY + y, sourceZ + z, tier);
    }

    protected boolean checkAir(int x, int y, int z) {
        return KilnValidationHelper.isAir(world, sourceX + x, sourceY + y, sourceZ + z);
    }

    protected boolean checkAirOrPottery(int x, int y, int z) {
        return KilnValidationHelper.isAirOrPottery(world, sourceX + x, sourceY + y, sourceZ + z);
    }

    protected boolean checkWall(int x, int y, int z, ForgeDirection d) {
        return KilnValidationHelper.isWall(world, sourceX + x, sourceY + y, sourceZ + z, d);
    }

    protected boolean checkAirOrFire(int x, int y, int z) {
        return KilnValidationHelper.isAirOrFire(world, sourceX + x, sourceY + y, sourceZ + z);
    }

    protected boolean checkFireBrick(int x, int y, int z) {
        return KilnValidationHelper.isFireBrick(world, sourceX + x, sourceY + y, sourceZ + z);
    }

}
