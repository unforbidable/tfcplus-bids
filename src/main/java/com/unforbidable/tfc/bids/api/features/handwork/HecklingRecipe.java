package com.unforbidable.tfc.bids.api.features.handwork;

import net.minecraft.item.ItemStack;

public class HecklingRecipe extends HandworkRecipe {

    public HecklingRecipe(ItemStack input, ItemStack output, int duration) {
        super(input, output, duration);
    }

    public HecklingRecipe(ItemStack input, ItemStack output, ItemStack extra, int duration) {
        super(input, output, extra, duration);
    }

}
