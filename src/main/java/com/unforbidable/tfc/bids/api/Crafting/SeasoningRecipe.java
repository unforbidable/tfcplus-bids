package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class SeasoningRecipe {

    final ItemStack output;
    final ItemStack input;
    final float durationMultipliter;

    public SeasoningRecipe(ItemStack output, ItemStack input, float durationMultipliter) {
        this.output = output;
        this.input = input;
        this.durationMultipliter = durationMultipliter;
    }

    public boolean matches(ItemStack itemStack) {
        return itemStack.getItem() == input.getItem()
                && itemStack.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getCraftingResult(ItemStack itemStack) {
        return output;
    }

    public float getDurationMultipliter(ItemStack itemStack) {
        return durationMultipliter;
    }

}
