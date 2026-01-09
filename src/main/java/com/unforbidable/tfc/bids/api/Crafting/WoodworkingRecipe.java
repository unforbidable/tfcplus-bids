package com.unforbidable.tfc.bids.api.Crafting;

import net.minecraft.item.ItemStack;

public class WoodworkingRecipe {

    private final String planName;
    private final ItemStack input;
    private final ItemStack output;

    public WoodworkingRecipe(String planName, ItemStack input, ItemStack output) {
        this.planName = planName;
        this.input = input;
        this.output = output;
    }

    public String getPlanName() {
        return planName;
    }

    public ItemStack getInput() {
        return input;
    }

    public ItemStack getOutput() {
        return output;
    }

    public boolean matches(ItemStack ingredient) {
        return ingredient.getItem() == input.getItem()
            && ingredient.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getResult(ItemStack ingredient) {
        return getOutput().copy();
    }

}
