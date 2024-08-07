package com.unforbidable.tfc.bids.Core.Wood;

import com.dunk.tfc.api.Enums.EnumFuelMaterial;

public class WoodBuilder {

    private final int index;
    private final String name;
    private int maxBurnTemp;
    private int maxBurnTime;
    private int tasteProfile;
    private boolean hasBark;
    private boolean hasBarkFibers;
    private boolean hasBarkTannin;
    private boolean hasLargeLogs;
    private boolean hardwood;
    private boolean resinous;
    private boolean inflammable;

    public WoodBuilder(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public WoodBuilder setMaxBurnTemp(int maxBurnTemp) {
        this.maxBurnTemp = maxBurnTemp;

        return this;
    }

    public WoodBuilder setMaxBurnTime(int maxBurnTime) {
        this.maxBurnTime = maxBurnTime;

        return this;
    }

    public WoodBuilder setTasteProfile(int tasteProfile) {
        this.tasteProfile = tasteProfile;

        return this;
    }

    public WoodBuilder setHasBark() {
        this.hasBark = true;

        return this;
    }

    public WoodBuilder setHasBarkFibers() {
        this.hasBark = true;
        this.hasBarkFibers = true;

        return this;
    }

    public WoodBuilder setHasBarkTannin() {
        this.hasBark = true;
        this.hasBarkTannin = true;

        return this;
    }

    public WoodBuilder setHasLargeLogs() {
        this.hasLargeLogs = true;

        return this;
    }

    public WoodBuilder setHardwood() {
        this.hardwood = true;

        return this;
    }

    public WoodBuilder setResinous() {
        this.resinous = true;

        return this;
    }

    public WoodBuilder setInflammable() {
        this.inflammable = true;

        return this;
    }

    public WoodBuilder setFuelMaterial(EnumFuelMaterial fuelMaterial) {
        this.maxBurnTemp = fuelMaterial.burnTempMax;
        this.maxBurnTime = fuelMaterial.burnTimeMax;
        this.tasteProfile = fuelMaterial.ordinal();

        return this;
    }

    public WoodIndex build() {
        return new WoodIndex(index, name, maxBurnTemp, maxBurnTime, tasteProfile, hasBark, hasBarkFibers, hasBarkTannin, hasLargeLogs, hardwood, resinous, inflammable);
    }

}
