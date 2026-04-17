package com.unforbidable.tfc.bids.api.Crafting;

import com.unforbidable.tfc.bids.api.Interfaces.ISimpleRecipeMatcher;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WoodworkingRecipe implements ISimpleRecipeMatcher<ItemStack> {

    protected final String planName;
    protected final ItemStack input;
    protected final ItemStack output;

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

    @Override
    public boolean matches(ItemStack ingredient) {
        return ingredient.getItem() == input.getItem()
            && ingredient.getItemDamage() == input.getItemDamage();
    }

    public ItemStack getResult(ItemStack ingredient) {
        return getOutput().copy();
    }

    public List<ItemStack> getIngredients() {
        return new ArrayList<ItemStack>() {{ add(input); }};
    }

}
