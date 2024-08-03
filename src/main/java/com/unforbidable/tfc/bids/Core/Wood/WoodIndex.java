package com.unforbidable.tfc.bids.Core.Wood;

public class WoodIndex {

    public final WoodItemProvider items = WoodScheme.DEFAULT.getWoodItemProvider(this);
    public final WoodBlockProvider blocks = WoodScheme.DEFAULT.getWoodBlockProvider(this);

    public final int index;
    public final String name;
    public final int maxBurnTemp;
    public final int maxBurnTime;
    public final int tasteProfile;
    public final boolean hasBark;
    public final boolean hasBarkFibers;
    public final boolean hasBarkTannin;
    public final boolean hasLargeLogs;
    public final boolean hardwood;
    public final boolean resinous;
    public final boolean inflammable;

    public WoodIndex(int index, String name, int maxBurnTemp, int maxBurnTime, int tasteProfile, boolean hasBark, boolean hasBarkFibers, boolean hasBarkTannin, boolean hasLargeLogs, boolean hardwood, boolean resinous, boolean inflammable) {
        this.index = index;
        this.name = name;
        this.maxBurnTemp = maxBurnTemp;
        this.maxBurnTime = maxBurnTime;
        this.tasteProfile = tasteProfile;
        this.hasBark = hasBark;
        this.hasBarkFibers = hasBarkFibers;
        this.hasBarkTannin = hasBarkTannin;
        this.hasLargeLogs = hasLargeLogs;
        this.hardwood = hardwood;
        this.resinous = resinous;
        this.inflammable = inflammable;
    }

}
