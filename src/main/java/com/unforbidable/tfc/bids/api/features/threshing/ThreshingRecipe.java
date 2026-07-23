package com.unforbidable.tfc.bids.api.features.threshing;

import com.unforbidable.tfc.bids.api.util.SimpleRecipeMatcher;
import net.minecraft.item.ItemStack;

public class ThreshingRecipe implements SimpleRecipeMatcher<ItemStack> {

    private final ItemStack output;
    private final ItemStack input;
    private final ItemStack extra;
    private final int duration;

    public ThreshingRecipe(ItemStack output, ItemStack input, ItemStack extra, int duration) {
        this.output = output;
        this.input = input;
        this.extra = extra;
        this.duration = duration;
    }

    public ThreshingRecipe(ItemStack output, ItemStack input, int duration) {
        this(output, input, null, duration);
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
        ItemStack extra = getExtra();
        return extra != null ? extra.copy() : null;
    }

}
