package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.TFCItems;
import net.minecraft.item.ItemStack;

public class ItemStuffedPepper extends ItemMoreSandwich {

    public ItemStuffedPepper(float[] ingredientWeights, float foodMinWeight) {
        super(ingredientWeights, foodMinWeight);
    }

    @Override
    public int getDamageFromIngredients(ItemStack[] input) {
        if (input[0].getItem() == TFCItems.greenBellPepper)
            return 0;
        else if (input[0].getItem() == TFCItems.yellowBellPepper)
            return 1;
        else if (input[0].getItem() == TFCItems.redBellPepper)
            return 2;
        else
            return 0;
    }

}
