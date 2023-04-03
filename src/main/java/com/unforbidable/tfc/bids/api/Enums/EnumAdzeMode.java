package com.unforbidable.tfc.bids.api.Enums;

public enum EnumAdzeMode {
    SINGLE,
    DOUBLE;

    public static EnumAdzeMode[] ALL_MODES = new EnumAdzeMode[] { SINGLE, DOUBLE };

    public EnumAdzeMode getNext() {
        int next = ordinal() + 1;
        if (next == ALL_MODES.length) {
            return SINGLE;
        } else {
            return ALL_MODES[next];
        }
    }
}
