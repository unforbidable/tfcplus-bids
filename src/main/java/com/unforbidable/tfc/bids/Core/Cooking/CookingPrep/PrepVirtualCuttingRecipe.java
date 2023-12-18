package com.unforbidable.tfc.bids.Core.Cooking.CookingPrep;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.unforbidable.tfc.bids.api.Crafting.PrepIngredient;
import com.unforbidable.tfc.bids.api.Crafting.PrepIngredientSpec;
import com.unforbidable.tfc.bids.api.Crafting.PrepRecipe;
import net.minecraft.item.ItemStack;

public class PrepVirtualCuttingRecipe extends PrepRecipe {

    private static final float[] weightsAll = { 0, 80, 40, 20, 10 };

    private final float minWeight;
    private final float[] weights = new float[INGREDIENT_COUNT];

    public PrepVirtualCuttingRecipe(ItemStack output, PrepIngredientSpec[] ingredients, int slot) {
        super(output, ingredients);

        if (slot < 1 || slot > 4) {
            throw new IllegalArgumentException("Slot for cutting food must be 1 to 4");
        }

        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (i == slot) {
                weights[i] = weightsAll[i];
            } else {
                weights[i] = 0;
            }
        }

        this.minWeight = weightsAll[slot];
    }

    @Override
    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        if (input.length != INGREDIENT_COUNT) {
            throw new IllegalArgumentException("Expected array of ingredients of size " + INGREDIENT_COUNT);
        }

        ItemStack result = getOutput().copy();

        ItemStack[] consumedIngredients = getConsumedIngredients(input, consumeIngredients);

        float weight = getTotalConsumedIngredientWeight(consumedIngredients);
        ItemFoodTFC.createTag(result, weight);

        return result;
    }

    @Override
    public float getMinWeight() {
        return minWeight;
    }

    public static PrepVirtualCuttingRecipe forIngredientInSlot(ItemStack ingredient, int slot) {
        ItemStack output = ItemFoodTFC.createTag(ingredient.copy(), weightsAll[slot]);

        PrepIngredientSpec[] ingredients = new PrepIngredientSpec[INGREDIENT_COUNT];
        for (int i = 0; i < INGREDIENT_COUNT; i++) {
            if (i == slot) {
                ingredients[i] = PrepIngredient.from(ingredient.copy()).toSpec(weightsAll[i]);
            }
        }

        return new PrepVirtualCuttingRecipe(output, ingredients, slot);
    }

}
