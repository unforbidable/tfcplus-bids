package com.unforbidable.tfc.bids.api.Enums;

public enum EnumWallHookPos {
    HIGH(0),
    MID(4 / 32f),
    LOW(8 / 32f);

    private final float offset;

    EnumWallHookPos(float offset) {
        this.offset = offset;
    }

    public float getOffset() {
        return offset;
    }

}
