package com.unforbidable.tfc.bids.api.Enums;

public enum EnumLogWallType {

    EAST,
    NORTH,
    CORNER,
    EAST_ALT,
    NORTH_ALT,
    CORNER_ALT;

    public static final EnumLogWallType[] ALL_TYPES = new EnumLogWallType[] { EAST, NORTH, CORNER,
            EAST_ALT, NORTH_ALT, CORNER_ALT };

    private static String[] NAMES = new String[] { "EAST", "NORTH", "CORNER",
            "EAST_ALT", "NORTH_ALT", "CORNER_ALT" };

    private EnumLogWallType() {
    }

    @Override
    public String toString() {
        return NAMES[ordinal()];
    }

}
