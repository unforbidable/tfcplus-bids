package com.unforbidable.tfc.bids.Core.Kilns;

import net.minecraft.world.World;

public abstract class KilnValidator<TResultParams> {

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

}
