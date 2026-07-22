package com.unforbidable.tfc.bids.features.utility.compositetools.main;

import com.dunk.tfc.api.Crafting.AnvilManager;
import com.unforbidable.tfc.bids.util.ore.OreDictionaryHelper;
import net.minecraft.item.ItemStack;
import java.util.Arrays;
import java.util.List;

public class CompositeToolHelper {

    private static final List<String> compositeToolOres = Arrays.asList(
        "itemAxeStone", "itemHammerStone", "itemKnifeStone", "itemShovelStone", "itemHoeStone", "itemJavelinStone",
        "itemAdzeStone", "itemDrillStone"
    );

    public static void applyCompositeToolBindingBonus(ItemStack tool, ItemStack binding) {
        float bindingBonus = getBindingBonus(binding);
        if (bindingBonus > 0) {
            AnvilManager.setDurabilityBuff(tool, bindingBonus);
        }
    }

    private static float getBindingBonus(ItemStack binding) {
        switch (getBindingQuality(binding)) {
            case STRONG:
                return 1f;

            case POOR:
                return 0.2f;
        }

        return 0;
    }

    public static BindingQuality getBindingQuality(ItemStack itemStack) {
        if (OreDictionaryHelper.itemStackIsOre(itemStack, "materialBinding")) {
            if (OreDictionaryHelper.itemStackIsOre(itemStack, "materialBindingStrong")) {
                return BindingQuality.STRONG;
            } else {
                return BindingQuality.POOR;
            }
        } else {
            return BindingQuality.NONE;
        }
    }

    public static boolean isCompositeTool(ItemStack tool) {
        return OreDictionaryHelper.itemStackIsAnyOre(tool, compositeToolOres);
    }

}
