package com.unforbidable.tfc.bids.Core.Stone;

public class StoneBuilder {

    private final int index;
    private final String name;
    private boolean soft;
    private boolean quern;
    private boolean hasShingle;

    public StoneBuilder(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public StoneBuilder setSoft(boolean soft) {
        this.soft = soft;

        return this;
    }

    public StoneBuilder setQuern(boolean quern) {
        this.quern = quern;

        return this;
    }

    public StoneBuilder setHasShingle(boolean hasShingle) {
        this.hasShingle = hasShingle;

        return this;
    }

    public StoneIndex build() {
        return new StoneIndex(index, name, soft, quern, hasShingle);
    }

}
