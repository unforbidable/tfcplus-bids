package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class SpinningRecipe {

    private final ItemStack output;
    private final ItemStack input;
    private final int duration;

    public SpinningRecipe(ItemStack output, ItemStack input, int duration) {
        this.output = output;
        this.input = input;
        this.duration = duration;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public int getDuration() {
        return duration;
    }

    public boolean matches(ItemStack ingredient) {
        return matchesIngredient(ingredient);
    }

    public boolean matchesIngredient(ItemStack ingredient) {
        return ingredient.getItem() == input.getItem()
            && ingredient.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getResult(ItemStack ingredient) {
        return getOutput().copy();
    }

}
