package com.unforbidable.tfc.bids.Core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class OreDictionaryHelper {

    public static boolean itemMatchesOre(ItemStack is, List<ItemStack> oreList, boolean strict) {
        for (ItemStack ore : oreList) {
            if (OreDictionary.itemMatches(ore, is, strict)) {
                return true;
            }
        }

        return false;
    }

    public static boolean itemMatchesOre(ItemStack is, String oreName, boolean strict) {
        if (OreDictionary.doesOreNameExist(oreName)) {
            return itemMatchesOre(is, OreDictionary.getOres(oreName), strict);
        } else {
            return false;
        }
    }

}
