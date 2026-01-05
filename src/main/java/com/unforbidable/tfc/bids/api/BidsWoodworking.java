package com.unforbidable.tfc.bids.api;

import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingActionGroup;
import com.unforbidable.tfc.bids.api.Interfaces.IWoodworkingMaterial;

public class BidsWoodworking {

    public static class Materials {

        public static IWoodworkingMaterial LOG;
        public static IWoodworkingMaterial BOARD;

    }

    public static class Actions {

        public static IWoodworkingActionGroup DRILL_DRILL;
        public static IWoodworkingActionGroup SAW_CUT;
        public static IWoodworkingActionGroup AXE_CHOP;
        public static IWoodworkingActionGroup AXE_CARVE;
        public static IWoodworkingActionGroup KNIFE_CARVE;
        public static IWoodworkingActionGroup CHISEL_CUT;

    }

}
