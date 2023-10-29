package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class PrepSandwichRecipe extends PrepRecipe {

    public PrepSandwichRecipe(ItemStack output, PrepIngredient[] ingredients) {
        super(output, ingredients);
    }

    @Override
    public float[] getIngredientWeights() {
        return new float[] {2, 3, 2, 2, 1};
    }

    @Override
    public float getMinWeight() {
        return 7;
    }

}
