package com.unforbidable.tfc.bids.features.crafting.cooking.eventhandler;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.api.util.food.BidsFood;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import java.util.List;

public class CookedFoodTooltipHandler {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (event.itemStack.getItem() instanceof ItemFoodTFC) {
            handleFood(event.toolTip, event.itemStack);
        }
    }

    private void handleFood(List<String> toolTip, ItemStack itemStack) {
        if (BidsFood.isBoiled(itemStack)) {
            toolTip.add(2, StatCollector.translateToLocal(EnumChatFormatting.AQUA + StatCollector.translateToLocal("word.boiled")));
        } else if (BidsFood.isSteamed(itemStack)) {
            toolTip.add(2, StatCollector.translateToLocal(EnumChatFormatting.AQUA + StatCollector.translateToLocal("word.steamed")));
        }
    }

}
