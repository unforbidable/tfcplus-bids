package com.unforbidable.tfc.bids.api.features.carving;

import com.unforbidable.tfc.bids.features.building.carving.main.modes.CarvingModeCorner;
import com.unforbidable.tfc.bids.features.building.carving.main.modes.CarvingModeSide;
import com.unforbidable.tfc.bids.features.building.carving.main.modes.CarvingModeSingle;

public enum AdzeMode {

    SINGLE(new CarvingModeSingle()),
    CORNER(new CarvingModeCorner()),
    SIDE(new CarvingModeSide());

    public static final AdzeMode DEFAULT_MODE = SINGLE;
    public static final AdzeMode[] ALL_MODES = new AdzeMode[] { SINGLE, CORNER, SIDE };

    private final CarvingMode carvingMode;

    AdzeMode(CarvingMode carvingMode) {
        this.carvingMode = carvingMode;
    }

    public static AdzeMode valueOf(int ordinal) {
        return ALL_MODES[ordinal];
    }

    public CarvingMode getCarvingMode() {
        return carvingMode;
    }

    public AdzeMode getNext() {
        int next = ordinal() + 1;
        if (next == ALL_MODES.length) {
            return DEFAULT_MODE;
        } else {
            return ALL_MODES[next];
        }
    }

}
