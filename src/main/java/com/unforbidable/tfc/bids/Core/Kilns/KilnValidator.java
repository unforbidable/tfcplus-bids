package com.unforbidable.tfc.bids.Core.Kilns;

import net.minecraft.world.World;

public abstract class KilnValidator<TResult extends KilnValidatorResult> {

    protected final World world;
    protected final int sourceX;
    protected final int sourceY;
    protected final int sourceZ;

    TResult result;

    public KilnValidator(World world, int sourceX, int sourceY, int sourceZ) {
        this.world = world;
        this.sourceX = sourceX;
        this.sourceY = sourceY;
        this.sourceZ = sourceZ;
    }

    public boolean isValid() {
        return getResult().valid;
    }

    protected TResult getResult() {
        if (result == null) {
            result = validateStructure();
        }

        return result;
    }

    protected abstract TResult validateStructure();

}
