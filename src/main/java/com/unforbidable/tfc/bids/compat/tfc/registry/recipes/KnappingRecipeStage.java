package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.dunk.tfc.api.Crafting.CraftingManagerTFC;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class KnappingRecipeStage extends RegistryStage<KnappingRecipe> {

    public static final KnappingRecipeStage instance = new KnappingRecipeStage();

    @Override
    public void add(KnappingRecipe recipe) {
        Bids.LOG.debug("Register TFC knapping recipe for {}", recipe.output);

        try {
            CraftingManagerTFC.getInstance().addRecipe(recipe.output, recipe.input);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to register TFC knapping recipe for {}: {}", recipe.output, ex.getMessage(), ex);
        }
    }

}
