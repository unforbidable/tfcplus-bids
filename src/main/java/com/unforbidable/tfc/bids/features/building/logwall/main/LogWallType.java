package com.unforbidable.tfc.bids.features.building.logwall.main;

public enum LogWallType {

    EAST,
    NORTH,
    CORNER,
    EAST_ALT,
    NORTH_ALT,
    CORNER_ALT;

    public static final LogWallType[] ALL_TYPES = new LogWallType[] { EAST, NORTH, CORNER,
            EAST_ALT, NORTH_ALT, CORNER_ALT };

    private static final String[] names = new String[] { "EAST", "NORTH", "CORNER",
            "EAST_ALT", "NORTH_ALT", "CORNER_ALT" };

    LogWallType() {
    }

    @Override
    public String toString() {
        return names[ordinal()];
    }

}
