package com.unforbidable.tfc.bids.util.wood;

import com.dunk.tfc.api.Constant.Global;

public class WoodHelper {

    public static String[] getWoodOffsetNames(int offset) {
        final int length = Math.min(16, Global.WOOD_ALL.length - offset);
        final String[] names = new String[length];
        for (int i = 0; i < length; i++) {
            names[i] = Global.WOOD_ALL[i + offset];
        }
        return names;
    }

}
