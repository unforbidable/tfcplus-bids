package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.Core.Carving.CarvingBitMap;

import net.minecraft.item.ItemStack;

public class CarvingRecipe {

    final ItemStack output;
    final ItemStack input;
    final CarvingRecipePattern pattern;

    public CarvingRecipe(ItemStack output, ItemStack input, CarvingRecipePattern pattern) {
        this.output = output;
        this.input = input;
        this.pattern = pattern;
    }

    public ItemStack getInput() {
        return input;
    }

    public CarvingRecipePattern getPattern() {
        return pattern;
    }

    public boolean matchCarving(ItemStack ingredient, CarvingBitMap carvedBits) {
        return matchCarving(ingredient) && pattern.matchCarving(carvedBits);
    }

    public boolean matchCarving(ItemStack ingredient) {
        return ingredient.getItem() == input.getItem()
                && ingredient.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getCraftingResult() {
        return output.copy();
    }

}
