package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Interfaces.IMoreSandwich;
import net.minecraft.item.ItemStack;

public class PrepMoreSandwichRecipe extends PrepRecipe {

    public PrepMoreSandwichRecipe(ItemStack output, PrepIngredient[] ingredients) {
        super(output, ingredients);

        if (!(output.getItem() instanceof IMoreSandwich)) {
            throw new IllegalArgumentException("PrepMoreSandwichRecipe requires output to be derived from ItemMoreSandwich or implement IMoreSandwich");
        }
    }

    @Override
    public float[] getIngredientWeights() {
        return ((IMoreSandwich)getOutput().getItem()).getIngredientWeights();
    }

    @Override
    public float getMinWeight() {
        return ((IMoreSandwich)getOutput().getItem()).getFoodMinWeight();
    }

}
