package com.unforbidable.tfc.bids.api.Enums;

import com.unforbidable.tfc.bids.Core.Carving.Modes.CarvingModeCorner;
import com.unforbidable.tfc.bids.Core.Carving.Modes.CarvingModeSide;
import com.unforbidable.tfc.bids.Core.Carving.Modes.CarvingModeSingle;
import com.unforbidable.tfc.bids.api.Interfaces.ICarvingMode;

public enum EnumAdzeMode {

    SINGLE(new CarvingModeSingle()),
    CORNER(new CarvingModeCorner()),
    SIDE(new CarvingModeSide());

    public static final EnumAdzeMode DEFAULT_MODE = SINGLE;
    public static final EnumAdzeMode[] ALL_MODES = new EnumAdzeMode[] { SINGLE, CORNER, SIDE };

    private final ICarvingMode carvingMode;

    EnumAdzeMode(ICarvingMode carvingMode) {
        this.carvingMode = carvingMode;
    }

    public static EnumAdzeMode valueOf(int ordinal) {
        return ALL_MODES[ordinal];
    }

    public ICarvingMode getCarvingMode() {
        return carvingMode;
    }

    public EnumAdzeMode getNext() {
        int next = ordinal() + 1;
        if (next == ALL_MODES.length) {
            return DEFAULT_MODE;
        } else {
            return ALL_MODES[next];
        }
    }

}
