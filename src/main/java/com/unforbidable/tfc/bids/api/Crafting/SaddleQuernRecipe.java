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
                        || foodMatches(itemStack));
    }

    private boolean foodMatches(ItemStack itemStack) {
        ItemStack inputFood = ItemFoodTFC.createTag(input.copy());

        if (Food.areEqual(inputFood, itemStack)) {
            return true;
        }

        // Allow cooked - set both to 0 and compare
        if (Food.getCooked(itemStack) > 0) {
            ItemStack inputFoodCooked = inputFood.copy();
            Food.setCooked(inputFoodCooked,0);
            ItemStack itemStackCooked = itemStack.copy();
            Food.setCooked(itemStackCooked,0);

            return Food.areEqual(inputFoodCooked, itemStackCooked);
        }

        return false;
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public ItemStack getCraftingResult() {
        return output.copy();
    }

}
