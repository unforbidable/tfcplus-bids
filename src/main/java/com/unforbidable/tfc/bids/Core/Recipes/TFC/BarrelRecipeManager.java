package com.unforbidable.tfc.bids.Core.Recipes.TFC;

import com.dunk.tfc.api.Crafting.BarrelManager;
import com.unforbidable.tfc.bids.Bids;

public class BarrelRecipeManager {

    public static void addRecipe(BarrelRecipeBuilder builder) {
        try {
            Bids.LOG.info("Building TFC barrel recipe for " + builder.toString());
            BarrelManager.getInstance().addRecipe(builder.build());
        } catch (Exception ex) {
            Bids.LOG.warn("Unable to build TFC barrel recipe for " + builder.toString(), ex);
        }
    }

}
