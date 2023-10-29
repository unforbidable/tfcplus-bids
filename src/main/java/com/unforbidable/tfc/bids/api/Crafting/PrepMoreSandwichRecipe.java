package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Interfaces.IMoreSandwich;
import net.minecraft.item.ItemStack;

public class PrepMoreSandwichRecipe extends PrepRecipe {

    public PrepMoreSandwichRecipe(ItemStack output, PrepIngredient[] ingredients) {
        super(output, ingredients);

        if (!(output.getItem() instanceof IMoreSandwich)) {
            throw new IllegalArgumentException("PrepMoreSandwichRecipe requires output to be derived from ItemMoreSandwich or IMoreSandwich");
        }
    }

    @Override
    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        ItemStack result = super.getResult(input, consumeIngredients);

        int damage = ((IMoreSandwich)output.getItem()).getDamageFromIngredients(input);
        result.setItemDamage(damage);

        return result;
    }

    @Override
    public float[] getIngredientWeights() {
        return ((IMoreSandwich)output.getItem()).getIngredientWeights();
    }

    @Override
    public float getMinWeight() {
        return ((IMoreSandwich)output.getItem()).getFoodMinWeight();
    }

}
