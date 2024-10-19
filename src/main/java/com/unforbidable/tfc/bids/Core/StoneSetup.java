package com.unforbidable.tfc.bids.Core;

import com.dunk.tfc.api.Constant.Global;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Core.Stone.StoneBuilder;

import static com.unforbidable.tfc.bids.Core.Stone.StoneScheme.DEFAULT;

public class StoneSetup {

    public static void preInit() {
        registerStone();
    }

    private static void registerStone() {
        Bids.LOG.info("Registering stone");

        for (int i = 0; i < Global.STONE_IGIN.length; i ++) {
            DEFAULT.registerStone(new StoneBuilder(Global.STONE_IGIN_START + i, Global.STONE_IGIN[i])
                .setQuern(true)
                .build());
        }

        for (int i = 0; i < Global.STONE_SED.length; i ++) {
            DEFAULT.registerStone(new StoneBuilder(Global.STONE_SED_START + i, Global.STONE_SED[i])
                .setSoft(true)
                .build());
        }

        for (int i = 0; i < Global.STONE_IGEX.length; i ++) {
            DEFAULT.registerStone(new StoneBuilder(Global.STONE_IGEX_START + i, Global.STONE_IGEX[i])
                .setQuern(true)
                .build());
        }

        for (int i = 0; i < Global.STONE_MM.length; i ++) {
            DEFAULT.registerStone(new StoneBuilder(Global.STONE_MM_START + i, Global.STONE_MM[i])
                .setHasShingle(i == 1) // SLATE
                .setQuern(i == 4) // GNEISS
                .build());
        }
    }

}
