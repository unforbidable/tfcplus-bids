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
                String help = StatCollector.translateToLocal("gui.Help");
                if (!event.toolTip.contains(help)) {
                    event.toolTip.add(help);
                }

                for (String hint : hints) {
                    event.toolTip.add(StatCollector.translateToLocal("gui.Help." + hint));
                }
            } else {
                String showHelp = StatCollector.translateToLocal("gui.ShowHelp");
                if (!event.toolTip.contains(showHelp)) {
                    event.toolTip.add(showHelp);
                }
            }
        }
    }

}
