package com.unforbidable.tfc.bids.api.features.carving;

import com.unforbidable.tfc.bids.features.building.carving.main.CarvingBitMap;
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

    public boolean matches(ItemStack ingredient, CarvingBitMap carvedBits) {
        return matches(ingredient) && pattern.matchCarving(carvedBits);
    }

    public boolean matches(ItemStack ingredient) {
        return ingredient.getItem() == input.getItem()
                && ingredient.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getCraftingResult() {
        return output.copy();
    }

}
