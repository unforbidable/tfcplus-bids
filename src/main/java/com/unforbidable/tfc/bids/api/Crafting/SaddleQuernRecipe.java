package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class SaddleQuernRecipe {

    final ItemStack output;
    final ItemStack input;

    public SaddleQuernRecipe(ItemStack output, ItemStack input) {
        this.output = output;
        this.input = input;
    }

    public boolean matches(ItemStack itemStack) {
        return itemStack.getItem() == input.getItem()
                && itemStack.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public ItemStack getCraftingResult() {
        return output.copy();
    }

}
