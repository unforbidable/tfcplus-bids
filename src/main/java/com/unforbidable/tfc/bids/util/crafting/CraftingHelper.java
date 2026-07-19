package com.unforbidable.tfc.bids.util.crafting;

import com.dunk.tfc.api.Crafting.AnvilManager;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CraftingHelper {

    public static void applyCompositeToolBindingBonus(ItemStack tool, ItemStack binding) {
        float bindingBonus = getBestBindingBonus(binding);
        if (bindingBonus > 0) {
            AnvilManager.setDurabilityBuff(tool, bindingBonus);
        }
    }

    private static float getBestBindingBonus(ItemStack binding) {
        int poorOreId = OreDictionary.getOreID("materialBinding");
        int decentOreId = OreDictionary.getOreID("materialBindingDecent");
        int goodOreId = OreDictionary.getOreID("materialBindingStrong");

        int[] ids = OreDictionary.getOreIDs(binding);
        for (int id : ids) {
            if (id == goodOreId) {
                return 1f;
            }
        }

        for (int id : ids) {
            if (id == decentOreId) {
                return 0.5f;
            }
        }

        for (int id : ids) {
            if (id == poorOreId) {
                return 0.1f;
            }
        }

        return 0;
    }

    public static List<Integer> getStoneToolOreIds() {
        List<Integer> oreIds = new ArrayList<Integer>();
        for (String ore : getStoneToolOreNames()) {
            oreIds.add(OreDictionary.getOreID(ore));
        }
        return oreIds;
    }

    public static String[] getStoneToolOreNames() {
        return new String[]{"itemAxeStone", "itemHammerStone", "itemKnifeStone", "itemShovelStone", "itemHoeStone", "itemJavelinStone", "itemAdzeStone", "itemDrillStone"};
    }
}
