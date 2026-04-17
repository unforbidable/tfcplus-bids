package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Interfaces.ISimpleRecipeMatcher;
import net.minecraft.item.ItemStack;

public class HandworkRecipe implements ISimpleRecipeMatcher<ItemStack> {

    private final ItemStack output;
    private final ItemStack input;
    private final int duration;

    public HandworkRecipe(ItemStack output, ItemStack input, int duration) {
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

    @Override
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
