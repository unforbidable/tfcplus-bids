package com.unforbidable.tfc.bids.util.ore;

import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

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

    public static boolean itemStackIsOre(ItemStack itemStack, String oreName) {
        for (ItemStack ore : OreDictionary.getOres(oreName)) {
            if (OreDictionary.itemMatches(ore, itemStack, false)) {
                return true;
            }
        }

        return false;
    }

    public static boolean itemStackIsAnyOre(ItemStack tool, List<String> ores) {
        for (String ore : ores) {
            if (itemStackIsOre(tool, ore)) {
                return true;
            }
        }

        return false;
    }

}
