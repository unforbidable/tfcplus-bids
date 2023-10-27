package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.api.TFCItems;
import net.minecraft.item.ItemStack;

public class PrepSandwichRecipe extends PrepRecipe {

    public PrepSandwichRecipe(ItemStack output, PrepIngredient[] ingredients) {
        super(output, ingredients);
    }

    @Override
    public int[] getIngredientWeights() {
        return new int[] {2, 3, 2, 2, 1};
    }

    @Override
    public int getMinWeight() {
        return 7;
    }

    @Override
    public ItemStack getResult(ItemStack[] input, boolean consumeIngredients) {
        ItemStack result =  super.getResult(input, consumeIngredients);

        setSandwichIcon(result, input[0]);

        return result;
    }

    private void setSandwichIcon(ItemStack sandwich, ItemStack bread) {
        if (bread.getItem() == TFCItems.wheatBread)
            sandwich.setItemDamage(0);
        else if (bread.getItem() == TFCItems.oatBread)
            sandwich.setItemDamage(1);
        else if (bread.getItem() == TFCItems.barleyBread)
            sandwich.setItemDamage(2);
        else if (bread.getItem() == TFCItems.ryeBread)
            sandwich.setItemDamage(3);
        else if (bread.getItem() == TFCItems.cornBread)
            sandwich.setItemDamage(4);
        else if (bread.getItem() == TFCItems.riceBread)
            sandwich.setItemDamage(5);
    }

}
