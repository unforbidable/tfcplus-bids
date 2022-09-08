package com.unforbidable.tfc.bids.api.Enums;

public enum EnumLogWallVertType {

    DEFAULT,
    ALT;

    public static final EnumLogWallVertType[] ALL_TYPES = new EnumLogWallVertType[] { DEFAULT, ALT };

    private static String[] NAMES = new String[] { "DEFAULT", "ALT" };

    private EnumLogWallVertType() {
    }

    @Override
    public String toString() {
        return NAMES[ordinal()];
    }

}
