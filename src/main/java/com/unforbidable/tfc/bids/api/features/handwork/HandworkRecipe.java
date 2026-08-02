package com.unforbidable.tfc.bids.api.features.handwork;

import com.unforbidable.tfc.bids.api.util.SimpleRecipeMatcher;
import net.minecraft.item.ItemStack;

public class HandworkRecipe implements SimpleRecipeMatcher<ItemStack> {

    private final ItemStack output;
    private final ItemStack input;
    private final ItemStack extra;
    private final int duration;

    public HandworkRecipe(ItemStack input, ItemStack output, int duration) {
        this(input, output, null, duration);
    }

    public HandworkRecipe(ItemStack input, ItemStack output, ItemStack extra, int duration) {
        this.output = output;
        this.input = input;
        this.extra = extra;
        this.duration = duration;
    }

    public ItemStack getOutput() {
        return output;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getExtra() {
        return extra;
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

    public ItemStack getExtraResult(ItemStack ingredient) {
        ItemStack extraResult = getExtra();
        return extraResult != null ? extraResult.copy() : null;
    }

}
