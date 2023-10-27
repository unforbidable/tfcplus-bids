package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class PrepSaladRecipe extends PrepRecipe {

    public PrepSaladRecipe(ItemStack output, PrepIngredient[] ingredients) {
        super(output, ingredients);
    }

    @Override
    public int[] getIngredientWeights() {
        return new int[] { 0, 10, 4, 4, 2 };
    }

    @Override
    public int getMinWeight() {
        return 14;
    }

    @Override
    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        ItemStack result =  super.getResult(input, consumeIngredients);

        // Set bowl meta so that the correct bowl is returned when salad is eaten
        result.stackTagCompound.setInteger("bowlMeta", input[0].getItemDamage());

        return result;
    }

}
