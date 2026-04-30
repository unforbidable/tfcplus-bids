package com.unforbidable.tfc.bids.Core.Crafting;

import net.minecraft.item.crafting.IRecipe;

import java.util.function.Consumer;

public class ActionableRecipe {

    public final IRecipe recipe;
    public final Consumer<CraftingContext> action;

    public ActionableRecipe(IRecipe recipe, Consumer<CraftingContext> action) {
        this.recipe = recipe;
        this.action = action;
    }

}
