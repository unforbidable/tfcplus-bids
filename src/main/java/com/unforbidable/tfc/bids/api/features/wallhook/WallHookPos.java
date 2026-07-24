package com.unforbidable.tfc.bids.api.features.wallhook;

public enum WallHookPos {
    HIGH(0),
    MID(4 / 32f),
    LOW(8 / 32f);

    private final float offset;

    WallHookPos(float offset) {
        this.offset = offset;
    }

    public float getOffset() {
        return offset;
    }

}
