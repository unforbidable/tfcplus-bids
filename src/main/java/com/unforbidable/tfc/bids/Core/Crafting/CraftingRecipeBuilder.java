package com.unforbidable.tfc.bids.Core.Crafting;

import net.minecraft.item.crafting.IRecipe;

import java.util.function.Consumer;

public class CraftingRecipeBuilder {

    private final IRecipe recipe;

    private Consumer<CraftingContext> action;

    public CraftingRecipeBuilder(IRecipe recipe) {
        this.recipe = recipe;
    }

    public CraftingRecipeBuilder action(Consumer<CraftingContext> action) {
        if (this.action == null) {
            this.action = action;
        } else {
            this.action = this.action.andThen(action);
        }

        return this;
    }

    public CraftingRecipe build() {
        return new CraftingRecipe(recipe, action);
    }

}
