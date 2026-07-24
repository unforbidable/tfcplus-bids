package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.dunk.tfc.api.Crafting.KilnCraftingManager;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class KilnRecipeStage extends RegistryStage<KilnRecipe> {

    public static final KilnRecipeStage instance = new KilnRecipeStage();

    @Override
    public void add(KilnRecipe recipe) {
        Bids.LOG.info("Register TFC kiln recipe for {}", recipe.result);

        try {
            com.dunk.tfc.api.Crafting.KilnRecipe tfcRecipe = new com.dunk.tfc.api.Crafting.KilnRecipe(recipe.input, recipe.level, recipe.result);
            KilnCraftingManager.getInstance().addRecipe(tfcRecipe);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to register TFC kiln recipe for {}: {}", recipe.result, ex.getMessage(), ex);
        }
    }

}
