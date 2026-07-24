package com.unforbidable.tfc.bids.compat.tfc.registry.recipes;

import com.dunk.tfc.api.Crafting.BarrelAlcoholRecipe;
import com.dunk.tfc.api.Crafting.BarrelLiquidToLiquidRecipe;
import com.dunk.tfc.api.Crafting.BarrelManager;
import com.dunk.tfc.api.Crafting.BarrelMultiItemRecipe;
import com.unforbidable.tfc.bids.Bids;
import com.unforbidable.tfc.bids.compat.tfc.registry.RegistryStage;
import com.unforbidable.tfc.bids.compat.tfc.registry.recipes.barrel.BarrelItemDemandingRecipe;

public class BarrelRecipeStage extends RegistryStage<BarrelRecipe> {

    public static final BarrelRecipeStage instance = new BarrelRecipeStage();

    @Override
    public void add(BarrelRecipe recipe) {
        if (recipe.inputItem != null) {
             String name = recipe.inputItem.getDisplayName();
             Bids.LOG.info("NAME: {}", name);
        }

        Bids.LOG.info("Register TFC barrel recipe for {}", recipe);

        try {
            com.dunk.tfc.api.Crafting.BarrelRecipe recipeTfc = getRecipeInstance(recipe)
                .setMinTechLevel(recipe.minTechLevel)
                .setSealedRecipe(recipe.sealed)
                .setSealTime(recipe.sealTime)
                .setRequiresCooked(recipe.requiresCooked)
                .setRemovesLiquid(recipe.removesLiquid)
                .setAllowAnyStack(recipe.allowAnyStack);

            BarrelManager.getInstance().addRecipe(recipeTfc);
        } catch (Exception ex) {
            Bids.LOG.error("Failed to register TFC anvil recipe for {}: {}", recipe, ex.getMessage(), ex);
        }
    }

    private com.dunk.tfc.api.Crafting.BarrelRecipe getRecipeInstance(BarrelRecipe recipe) {
        switch (recipe.type) {
            case ALCOHOL:
                return new BarrelAlcoholRecipe(recipe.inputItem, recipe.inputFluid, recipe.outputItem, recipe.outputFluid);
            case LIQUID_TO_LIQUID:
                return new BarrelLiquidToLiquidRecipe(recipe.inputFluid, recipe.secondaryInputFluid, recipe.outputFluid);
            case MULTI_ITEM:
                return new BarrelMultiItemRecipe(recipe.inputItem, recipe.inputFluid, recipe.outputItem, recipe.outputFluid)
                    .setKeepStackSize(recipe.keepStackSize);
            case ITEM_DEMANDING:
                return new BarrelItemDemandingRecipe(recipe.inputItem, recipe.inputFluid, recipe.outputItem, recipe.outputFluid);
            case SIMPLE:
            default:
                return new com.dunk.tfc.api.Crafting.BarrelRecipe(recipe.inputItem, recipe.inputFluid, recipe.outputItem, recipe.outputFluid);
        }
    }

}
