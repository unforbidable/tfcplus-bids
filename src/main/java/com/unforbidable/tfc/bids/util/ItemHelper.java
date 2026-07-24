package com.unforbidable.tfc.bids.util;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.TFC_ItemHeat;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

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

    @SuppressWarnings({"unchecked" })
    public static void addHeatInformation(ItemStack object, List arraylist) {
        com.dunk.tfc.Items.ItemTerra.addHeatInformation(object, arraylist);
    }

    @SuppressWarnings({"unchecked" })
    public static void addHeatStatusInformation(ItemStack itemStack, List list) {
        if (TFC_ItemHeat.hasTemp(itemStack)) {
            String s = "";
            if (HeatRegistry.getInstance().isTemperatureDanger(itemStack)) {
                s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.danger") + " | ";
            }

            if (HeatRegistry.getInstance().isTemperatureWeldable(itemStack)) {
                s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.weldable") + " | ";
            }

            if (HeatRegistry.getInstance().isTemperatureWorkable(itemStack)) {
                s += EnumChatFormatting.WHITE + TFC_Core.translate("gui.ingot.workable");
            }

            if (!"".equals(s))
                list.add(s);
        }
    }

}
