package com.unforbidable.tfc.bids.features.utility.compositetools.eventhandler;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.unforbidable.tfc.bids.features.utility.compositetools.main.CompositeToolHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class CompositeToolTooltipHandler {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (AnvilManager.getDurabilityBuff(event.itemStack) > 0 && CompositeToolHelper.isCompositeTool(event.itemStack)) {
            replaceSmithingBonusToolip(event.toolTip);
        }
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
