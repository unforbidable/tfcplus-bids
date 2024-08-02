package com.unforbidable.tfc.bids.Core.Wood;

public class WoodIndex {

    public final WoodItemProvider items = WoodScheme.DEFAULT.getWoodItemProvider(this);

    public final int index;
    public final String name;
    public final int maxBurnTemp;
    public final int maxBurnTime;
    public final int tasteProfile;
    public final boolean hasBarkFibers;
    public final boolean hasBarkTannin;
    public final boolean irregular;
    public final boolean hardwood;
    public final boolean resinous;
    public final boolean inflammable;
    public final boolean flammableFresh;

    public WoodIndex(int index, String name, int maxBurnTemp, int maxBurnTime, int tasteProfile, boolean hasBarkFibers, boolean hasBarkTannin, boolean irregular, boolean hardwood, boolean resinous, boolean inflammable, boolean flammableFresh) {
        this.index = index;
        this.name = name;
        this.maxBurnTemp = maxBurnTemp;
        this.maxBurnTime = maxBurnTime;
        this.tasteProfile = tasteProfile;
        this.hasBarkFibers = hasBarkFibers;
        this.hasBarkTannin = hasBarkTannin;
        this.irregular = irregular;
        this.hardwood = hardwood;
        this.resinous = resinous;
        this.inflammable = inflammable;
        this.flammableFresh = flammableFresh;
    }

}
