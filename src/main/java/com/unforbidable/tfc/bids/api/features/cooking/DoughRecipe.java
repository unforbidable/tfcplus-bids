package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.item.ItemStack;

public class DoughRecipe {

    ItemStack input;
    ItemStack output;

    public DoughRecipe(ItemStack input, ItemStack output) {
        this.input = input;
        this.output = output;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

}
