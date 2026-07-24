package com.unforbidable.tfc.bids.util.ore;

import java.util.Iterator;
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

    public static String findLikelyOreName(List<ItemStack> itemStacks) {
        for (String ore : OreDictionary.getOreNames()) {
            List<ItemStack> oreItemStacks = OreDictionary.getOres(ore, false);
            if (itemStackListsMatch(itemStacks, oreItemStacks)) {
                return ore;
            }
        }

        return null;
    }

    public static boolean itemStackListsMatch(List<ItemStack> list1, List<ItemStack> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        Iterator<ItemStack> it1 = list1.iterator();
        Iterator<ItemStack> it2 = list2.iterator();
        while (it1.hasNext()) {
            ItemStack is1 = it1.next();
            ItemStack is2 = it2.next();
            if (!OreDictionary.itemMatches(is1, is2, false)) {
                return false;
            }
        }

        return true;
    }

}
