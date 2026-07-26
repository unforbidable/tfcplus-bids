package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.dunk.tfc.api.Crafting.LoomManager;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;
import java.util.List;
import java.util.function.Predicate;

public class LoomRecipeStage extends RegistryStage<LoomRecipe> {

    public static final LoomRecipeStage INSTANCE = new LoomRecipeStage();

    @Override
    public void add(LoomRecipe recipe) {
        Bids.LOG.debug("Register TFC loom recipe {} -> {}",
            recipe.input, recipe.output);

        try {
            LoomManager.getInstance().addRecipe(new com.dunk.tfc.api.Crafting.LoomRecipe(recipe.input, recipe.output), recipe.resourceLocation);
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to Register TFC loom recipe {} -> {}: {}",
                recipe.input, recipe.output, ex.getMessage(), ex);
        }
    }

    @Override
    public void remove(Predicate<LoomRecipe> predicate) {
        try {
            List<com.dunk.tfc.api.Crafting.LoomRecipe> recipes = LoomManager.getInstance().getRecipes();
            for (int i = 0; i < recipes.size(); i++) {
                com.dunk.tfc.api.Crafting.LoomRecipe recipe = recipes.get(i);
                LoomRecipe r = new LoomRecipe(recipe.inItemStack, recipe.outItemStack, null);
                if (predicate.test(r)) {
                    recipes.remove(i--);
                    Bids.LOG.info("Original burlap from fiber loom recipe removed: " + recipe.getInItem().getDisplayName());
                }
            }
        } catch (Exception ex) {
            Bids.LOG.warn("Failed to match loom recipes for removal: {}",
                ex.getMessage(), ex);
        }
    }

}
