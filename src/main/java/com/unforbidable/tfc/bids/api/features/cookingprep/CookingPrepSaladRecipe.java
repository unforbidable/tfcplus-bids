package com.unforbidable.tfc.bids.api.features.cookingprep;

import net.minecraft.item.ItemStack;

public class CookingPrepSaladRecipe extends CookingPrepRecipe {

    public CookingPrepSaladRecipe(ItemStack output, float minWight, CookingPrepIngredientSpec ...ingredients) {
        super(output, minWight, ingredients);
    }

    @Override
    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        ItemStack result =  super.getResult(input, consumeIngredients);

        // Set bowl meta so that the correct bowl is returned when salad is eaten
        result.stackTagCompound.setInteger("bowlMeta", input[0].getItemDamage());

        return result;
    }

}
