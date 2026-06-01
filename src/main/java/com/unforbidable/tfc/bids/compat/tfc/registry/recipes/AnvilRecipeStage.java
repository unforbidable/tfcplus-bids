package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Crafting.AnvilReq;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;

public class AnvilRecipeStage extends RegistryStage<AnvilRecipe> {

    public static final AnvilRecipeStage instance = new AnvilRecipeStage();

    @Override
    public void add(AnvilRecipe recipe) {
        Bids.LOG.info("Register TFC anvil recipe for {}", recipe.output);

        try {
            com.dunk.tfc.api.Crafting.AnvilRecipe recipeTfc = recipe.welding ?
                new com.dunk.tfc.api.Crafting.AnvilRecipe(recipe.input, recipe.input2, AnvilReq.STONE, recipe.output) :
                new com.dunk.tfc.api.Crafting.AnvilRecipe(recipe.input, recipe.input2, recipe.plan, AnvilReq.STONE, recipe.output);

            recipeTfc.anvilreq = recipe.req;

            if (recipe.skill != null) {
                recipeTfc.addRecipeSkill(recipe.skill);
            }

            if (recipe.welding) {
                AnvilManager.getInstance().addWeldRecipe(recipeTfc);
            } else {
                AnvilManager.getInstance().addRecipe(recipeTfc);
            }

        } catch (Exception ex) {
            Bids.LOG.error("Failed to register TFC anvil recipe for {}: {}", recipe.output, ex.getMessage(), ex);
        }
    }

}
