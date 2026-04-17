package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Interfaces.ISimpleRecipeMatcher;
import net.minecraft.item.ItemStack;

public class SeasoningRecipe implements ISimpleRecipeMatcher<ItemStack> {

    final ItemStack output;
    final ItemStack input;
    final int duration;

    public SeasoningRecipe(ItemStack output, ItemStack input, int duration) {
        this.output = output;
        this.input = input;
        this.duration = duration;
    }

    @Override
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

    public int getDuration() {
        return duration;
    }

}
