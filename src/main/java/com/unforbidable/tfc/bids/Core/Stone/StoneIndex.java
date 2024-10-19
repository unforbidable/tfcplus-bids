package com.unforbidable.tfc.bids.Core.Stone;

public class StoneIndex {

    public final StoneItemProvider items = StoneScheme.DEFAULT.getStoneItemProvider(this);
    public final StoneBlockProvider blocks = StoneScheme.DEFAULT.getStoneBlockProvider(this);

    public final int index;
    public final String name;
    public final boolean soft;
    public final boolean quern;
    public final boolean hasShingle;

    public StoneIndex(int index, String name, boolean soft, boolean quern, boolean hasShingle) {
        this.index = index;
        this.name = name;
        this.soft = soft;
        this.quern = quern;
        this.hasShingle = hasShingle;
    }

}
