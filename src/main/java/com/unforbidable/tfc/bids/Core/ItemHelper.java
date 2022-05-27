package com.unforbidable.tfc.bids.Core;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ItemHelper {

    public static boolean showShiftInformation() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT
                && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public static boolean showCtrlInformation() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT
                && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
    }

}
