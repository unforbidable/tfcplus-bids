package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;

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
                && itemStack.getItemDamage() == input.getItemDamage()
                && (!(input.getItem() instanceof IFood)
                        || Food.areEqual(ItemFoodTFC.createTag(input.copy()), itemStack));
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public ItemStack getCraftingResult() {
        return output.copy();
    }

}
