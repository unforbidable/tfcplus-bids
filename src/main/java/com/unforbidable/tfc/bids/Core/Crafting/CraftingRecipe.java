package com.unforbidable.tfc.bids.Core.Crafting;

import net.minecraft.item.crafting.IRecipe;

import java.util.function.Consumer;

public class CraftingRecipe {

    public final IRecipe recipe;
    public final Consumer<CraftingContext> action;

    public CraftingRecipe(IRecipe recipe, Consumer<CraftingContext> action) {
        this.recipe = recipe;
        this.action = action;
    }

}
