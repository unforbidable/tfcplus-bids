package com.unforbidable.tfc.bids.core.crafting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class CraftingHandler {

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent e) {
        RecipeManager.handleItemCraftedEvent(e);
    }

}
