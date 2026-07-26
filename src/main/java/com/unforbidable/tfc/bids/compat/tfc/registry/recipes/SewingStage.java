package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.dunk.tfc.api.Crafting.ClothingManager;
import com.dunk.tfc.api.Crafting.SewingPattern;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class SewingStage extends RegistryStage<SewingRecipe> {

    public static final SewingStage INSTANCE = new SewingStage();

    @Override
    public void add(SewingRecipe recipe) {
        Bids.LOG.debug("Register TFC sewing recipe {}for {}",
            recipe.pattern == null ? "(repair) " : "", recipe.output);

        try {
            if (recipe.pattern != null) {
                ClothingManager.getInstance().addRecipe(new com.dunk.tfc.api.Crafting.SewingRecipe(
                    new SewingPattern(recipe.output, recipe.pattern, true), recipe.input
                ));
            } else {
                ClothingManager.getInstance().addRecipe(new com.dunk.tfc.api.Crafting.SewingRecipe(
                    new SewingPattern(recipe.output, true), recipe.input
                ).setRepairRecipe());
            }
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to Register TFC sewing recipe {}for {}: {}",
                recipe.pattern == null ? "(repair) " : "", recipe.output, ex.getMessage(), ex);
        }
    }

}
