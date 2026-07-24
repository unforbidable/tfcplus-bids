package com.unforbidable.tfc.bids.core.help;

import com.unforbidable.tfc.bids.util.ItemHelper;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class HelpTooltipEventHandler {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        List<String> hints = HelpRegistry.hints.stream()
            .filter(h -> h.matchesItemStack(event.itemStack))
            .map(h -> h.getHintForItemStack(event.itemStack))
            .collect(Collectors.toList());

        if (!hints.isEmpty()) {
            if (ItemHelper.showShiftInformation()) {
                event.toolTip.add(StatCollector.translateToLocal("gui.Help"));

                for (String hint : hints) {
                    event.toolTip.add(StatCollector.translateToLocal("gui.Help." + hint));
                }
            } else {
                event.toolTip.add(StatCollector.translateToLocal("gui.ShowHelp"));
            }
        }
    }

}
