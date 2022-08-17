package com.unforbidable.tfc.bids.Core.Recipes;

import java.util.ArrayList;
import java.util.List;

import com.unforbidable.tfc.bids.Bids;

import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class RecipeManager {

    static List<RecipeAction> actions = new ArrayList<RecipeAction>();

    public static void addAction(RecipeAction action) {
        actions.add(action);
    }

    public static void handleItemCraftedEvent(ItemCraftedEvent event) {
        for (RecipeAction action : actions) {
            if (action.eventMatches(event)) {
                Bids.LOG.debug("Recipe output matches action: " + action.getClass().getName());
                action.onItemCrafted(event);
            }
        }
    }

}
