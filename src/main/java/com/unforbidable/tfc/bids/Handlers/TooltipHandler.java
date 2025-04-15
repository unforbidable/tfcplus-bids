package com.unforbidable.tfc.bids.Handlers;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class TooltipHandler {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (AnvilManager.getDurabilityBuff(event.itemStack) > 0 && isStoneTool(event.itemStack)) {
            replaceSmithingBonusToolip(event.toolTip);
        }
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
