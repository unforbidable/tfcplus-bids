package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.api.TFCItems;
import net.minecraft.item.ItemStack;

import static com.unforbidable.tfc.bids.Items.ItemStuffedPepper.STUFFED_PEPPER_MIN_WEIGHT;
import static com.unforbidable.tfc.bids.Items.ItemStuffedPepper.STUFFED_PEPPER_WEIGHTS;

public class PrepStuffedPepperRecipe extends PrepRecipe {


    public PrepStuffedPepperRecipe(ItemStack output, PrepIngredient[] ingredients) {
        super(output, ingredients);
    }

    @Override
    public float[] getIngredientWeights() {
        return STUFFED_PEPPER_WEIGHTS;
    }

    @Override
    public float getMinWeight() {
        return STUFFED_PEPPER_MIN_WEIGHT;
    }

    @Override
    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        ItemStack result =  super.getResult(input, consumeIngredients);

        setStuffedPepperIcon(result, input[0]);

        return result;
    }

    private void setStuffedPepperIcon(ItemStack sandwich, ItemStack bread) {
        if (bread.getItem() == TFCItems.greenBellPepper)
            sandwich.setItemDamage(0);
        else if (bread.getItem() == TFCItems.yellowBellPepper)
            sandwich.setItemDamage(1);
        else if (bread.getItem() == TFCItems.redBellPepper)
            sandwich.setItemDamage(2);
    }

}
