package com.unforbidable.tfc.bids.features.building.logwall.main;

public enum LogWallVertType {

    DEFAULT,
    ALT;

    public static final LogWallVertType[] ALL_TYPES = new LogWallVertType[] { DEFAULT, ALT };

    private static final String[] names = new String[] { "DEFAULT", "ALT" };

    LogWallVertType() {
    }

    @Override
    public String toString() {
        return names[ordinal()];
    }

}
