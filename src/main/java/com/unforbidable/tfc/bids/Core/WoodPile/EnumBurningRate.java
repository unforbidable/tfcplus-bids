package com.unforbidable.tfc.bids.Core.WoodPile;

public enum EnumBurningRate {
    NONE(0),
    NORMAL(1),
    INCREASED(2);

    private final float burningRate;

    EnumBurningRate(float burningRate) {
        this.burningRate = burningRate;
    }

    public float getBurningRate() {
        return burningRate;
    }

}
