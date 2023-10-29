package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.item.ItemStack;

public interface IMoreSandwich {

    int getDamageFromIngredients(ItemStack[] input);

    float[] getIngredientWeights();

    float getFoodMinWeight();

}
