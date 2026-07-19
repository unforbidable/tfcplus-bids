package com.unforbidable.tfc.bids.core.crafting;

import java.util.function.Consumer;
import net.minecraft.item.crafting.IRecipe;

public class ActionableRecipe {

    public final IRecipe recipe;
    public final Consumer<CraftingContext> action;

    public ActionableRecipe(IRecipe recipe, Consumer<CraftingContext> action) {
        this.recipe = recipe;
        this.action = action;
    }

}
