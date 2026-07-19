package com.unforbidable.tfc.bids.core.crafting;

import java.util.function.Consumer;
import net.minecraft.item.crafting.IRecipe;

public class ActionableRecipeBuilder {

    private final IRecipe recipe;

    private Consumer<CraftingContext> action;

    public ActionableRecipeBuilder(IRecipe recipe) {
        this.recipe = recipe;
    }

    public ActionableRecipeBuilder action(Consumer<CraftingContext> action) {
        if (this.action == null) {
            this.action = action;
        } else {
            this.action = this.action.andThen(action);
        }

        return this;
    }

    public ActionableRecipe build() {
        return new ActionableRecipe(recipe, action);
    }

}
