package com.unforbidable.tfc.bids.Core.Kilns;

import com.dunk.tfc.TileEntities.TEPottery;
import net.minecraft.tileentity.TileEntity;

public class KilnHelper {

    public static boolean isPottery(TileEntity te) {
        return te instanceof TEPottery;
    }

    public static void firePottery(TileEntity te) {
        if (te instanceof TEPottery) {
            TEPottery pottery = (TEPottery) te;
            pottery.cookItems();
        }
    }

}
