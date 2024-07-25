package com.unforbidable.tfc.bids.Core.WoodPile;

public enum EnumBurningRate {
    NONE(0, 0),
    NORMAL(1, 1),
    INCREASED(2, 1.8f);

    private final float burnTimeMultiplier;
    private final float burnTempMultiplier;

    EnumBurningRate(float burnTimeMultiplier, float burnTempMultiplier) {
        this.burnTimeMultiplier = burnTimeMultiplier;
        this.burnTempMultiplier = burnTempMultiplier;
    }

    public float getBurnTimeMultiplier() {
        return burnTimeMultiplier;
    }

    public float getBurnTempMultiplier() {
        return burnTempMultiplier;
    }

}
