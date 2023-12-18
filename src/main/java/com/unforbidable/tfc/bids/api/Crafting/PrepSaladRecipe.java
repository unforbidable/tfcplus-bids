package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class PrepSaladRecipe extends PrepRecipe {

    public PrepSaladRecipe(ItemStack output, PrepIngredientSpec[] ingredients, float minWight) {
        super(output, ingredients, minWight);
    }

    @Override
    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        ItemStack result =  super.getResult(input, consumeIngredients);

        // Set bowl meta so that the correct bowl is returned when salad is eaten
        result.stackTagCompound.setInteger("bowlMeta", input[0].getItemDamage());

        return result;
    }

}
