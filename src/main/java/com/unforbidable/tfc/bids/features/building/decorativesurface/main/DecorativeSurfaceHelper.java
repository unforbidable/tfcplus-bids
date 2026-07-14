package com.unforbidable.tfc.bids.features.building.decorativesurface.main;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class DecorativeSurfaceHelper {

    public static boolean isDecorativeSurfaceItem(ItemStack itemStack) {
        int decorativeSurfaceItemOreId = OreDictionary.getOreID("itemDecorativeSurface");
        for (int oreId : OreDictionary.getOreIDs(itemStack)) {
            if (oreId == decorativeSurfaceItemOreId) {
                return true;
            }
        }

        return false;
    }

}
