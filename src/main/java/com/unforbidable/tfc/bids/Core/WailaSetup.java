package com.unforbidable.tfc.bids.Core;

import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.Tags;

import cpw.mods.fml.common.event.FMLInterModComms;

public class WailaSetup {

    public static void init() {
        registerCallbacks();
    }

    private static void registerCallbacks() {
        Bids.LOG.info("Register WAILA callbacks");

        FMLInterModComms.sendMessage("Waila", "register",
                Tags.PACKAGE + ".WAILA.WailaCrucible.callbackRegister");
    }

}
