package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.TFCItems;
import com.unforbidable.tfc.bids.Core.ItemHelper;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeHelper;
import com.unforbidable.tfc.bids.Core.Textile.EnumTextileHint;
import com.unforbidable.tfc.bids.api.BidsItems;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TooltipHandler {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (AnvilManager.getDurabilityBuff(event.itemStack) > 0 && isStoneTool(event.itemStack)) {
            replaceSmithingBonusToolip(event.toolTip);
        }

        handleTextileTooltipHints(event.toolTip, event.itemStack);
    }

    private void handleTextileTooltipHints(List<String> toolTip, ItemStack itemStack) {
        Set<EnumTextileHint> hints = new HashSet<EnumTextileHint>();

        if (isTextileForExtracting(itemStack)) {
            hints.add(EnumTextileHint.EXTRACTING);
        }
        if (isTextileForTwisting(itemStack)) {
            hints.add(EnumTextileHint.TWISTING);
        }
        if (isTextileForWeavingCloth(itemStack)) {
            hints.add(EnumTextileHint.WEAVING_CLOTH);
        }

        if (hints.size() > 0) {
            if (ItemHelper.showShiftInformation()) {
                toolTip.add(StatCollector.translateToLocal("gui.Help"));

                for (EnumTextileHint hint : hints) {
                    toolTip.add(StatCollector.translateToLocal("gui.Help.Textile." + hint.helpString));
                }
            } else {
                toolTip.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }
    }

    private boolean isTextileForTwisting(ItemStack itemStack) {
        return itemStack.getItem() == TFCItems.linenString;
    }

    private boolean isTextileForWeavingCloth(ItemStack itemStack) {
        return itemStack.getItem() == TFCItems.linenString;
    }

    private boolean isTextileForExtracting(ItemStack itemStack) {
        if (itemStack.getItem() == TFCItems.agave) {
            return true;
        }

        if (itemStack.getItem() == BidsItems.bark) {
            int itemBarkHasFibersOreId = OreDictionary.getOreID("itemBarkHasFibers");
            for (int id : OreDictionary.getOreIDs(itemStack)) {
                if (id == itemBarkHasFibersOreId) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isStoneTool(ItemStack tool) {
        for (String ore : RecipeHelper.getStoneToolOreNames()) {
            int stoneToolOreId = OreDictionary.getOreID(ore);
            for (int id : OreDictionary.getOreIDs(tool)) {
                if (id == stoneToolOreId) {
                    return true;
                }
            }
        }

        return false;
    }

    private void replaceSmithingBonusToolip(List<String> toolTip) {
        String smithingBonus = TFC_Core.translate("gui.SmithingBonus");
        String bindingBonus = TFC_Core.translate("gui.BindingBonus");

        for (int i = 0; i < toolTip.size(); i++) {
            if (toolTip.get(i).startsWith(smithingBonus)) {
                toolTip.set(i, toolTip.get(i).replace(smithingBonus, bindingBonus));
                break;
            }
        }
    }

}
