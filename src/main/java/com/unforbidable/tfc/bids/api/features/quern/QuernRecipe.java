package com.unforbidable.tfc.bids.api.features.quern;

import net.minecraft.item.ItemStack;

public class QuernRecipe {

    private final ItemStack input;
    private final ItemStack output;
    private final float resistance;

    public QuernRecipe(ItemStack input, ItemStack output, float resistance) {
        this.input = input;
        this.output = output;
        this.resistance = resistance;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public float getResistance() {
        return resistance;
    }

}
