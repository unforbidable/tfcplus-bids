package com.unforbidable.tfc.bids.Handlers;

import com.unforbidable.tfc.bids.Core.Crucible.CrucibleHelper;
import com.unforbidable.tfc.bids.Core.Recipes.RecipeManager;
import com.unforbidable.tfc.bids.api.BidsBlocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.item.Item;

public class CraftingHandler {

    @SubscribeEvent
    public void onItemCrafted(ItemCraftedEvent e) {
        if (e.crafting.getItem() == Item.getItemFromBlock(BidsBlocks.fireClayCrucible)) {
            CrucibleHelper.triggerCrucibleAchievement(e.player);
        }

        RecipeManager.handleItemCraftedEvent(e);
    }

}
