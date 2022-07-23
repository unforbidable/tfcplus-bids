package com.unforbidable.tfc.bids.Core;

import java.util.List;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.item.ItemStack;

public class ItemHelper {

    public static boolean showShiftInformation() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT
                && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public static boolean showCtrlInformation() {
        return FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT
                && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL);
    }

    public static void addSizeInformation(ItemStack object, List<String> arraylist) {
        com.dunk.tfc.Items.ItemTerra.addSizeInformation(object, arraylist);
    }

}
