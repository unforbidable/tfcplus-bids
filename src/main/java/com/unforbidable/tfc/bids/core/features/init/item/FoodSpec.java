package com.unforbidable.tfc.bids.core.features.init.item;

public class FoodSpec {

    public final float decayRate;
    public final float waterPercentage;
    public final boolean edible;
    public final boolean canBeUsedRaw;
    public final boolean poisonOnRaw;
    public final boolean guaranteedPoisonOnRaw;

    public FoodSpec(float decayRate, float waterPercentage, boolean edible, boolean canBeUsedRaw, boolean poisonOnRaw, boolean guaranteedPoisonOnRaw) {
        this.decayRate = decayRate;
        this.waterPercentage = waterPercentage;
        this.edible = edible;
        this.canBeUsedRaw = canBeUsedRaw;
        this.poisonOnRaw = poisonOnRaw;
        this.guaranteedPoisonOnRaw = guaranteedPoisonOnRaw;
    }

}
