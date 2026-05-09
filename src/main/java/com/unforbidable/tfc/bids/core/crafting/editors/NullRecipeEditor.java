package com.unforbidable.tfc.bids.core.crafting.editors;

import net.minecraft.item.crafting.IRecipe;

public class NullRecipeEditor extends RecipeEditor {

    public NullRecipeEditor() {
        super(null, new Object[0]);
    }

    @Override
    protected boolean tryUpsize() {
        return false;
    }

    @Override
    public IRecipe build() {
        return null;
    }

}
