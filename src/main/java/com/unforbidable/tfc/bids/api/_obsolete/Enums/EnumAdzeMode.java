package com.unforbidable.tfc.bids.api._obsolete.Enums;

import com.unforbidable.tfc.bids.features.building.carving.main.modes.CarvingModeCorner;
import com.unforbidable.tfc.bids.features.building.carving.main.modes.CarvingModeSide;
import com.unforbidable.tfc.bids.features.building.carving.main.modes.CarvingModeSingle;
import com.unforbidable.tfc.bids.api.features.carving.CarvingMode;

public enum EnumAdzeMode {

    SINGLE(new CarvingModeSingle()),
    CORNER(new CarvingModeCorner()),
    SIDE(new CarvingModeSide());

    public static final EnumAdzeMode DEFAULT_MODE = SINGLE;
    public static final EnumAdzeMode[] ALL_MODES = new EnumAdzeMode[] { SINGLE, CORNER, SIDE };

    private final CarvingMode carvingMode;

    EnumAdzeMode(CarvingMode carvingMode) {
        this.carvingMode = carvingMode;
    }

    public static EnumAdzeMode valueOf(int ordinal) {
        return ALL_MODES[ordinal];
    }

    public CarvingMode getCarvingMode() {
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
