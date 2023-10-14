package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.api.BidsFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class StonePressRecipe {

    final FluidStack output;
    final ItemStack input;

    public StonePressRecipe(FluidStack output, ItemStack input) {
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

        // Ignore cooked and infused status
        ItemStack inputFoodCooked = inputFood.copy();
        ItemStack itemStackCooked = itemStack.copy();
        Food.setCooked(itemStackCooked,0);
        Food.setCooked(inputFoodCooked,0);
        Food.setInfusion(itemStackCooked, "dummy");
        Food.setInfusion(inputFoodCooked, "dummy");

        // Use BidsFood to check boiled and steamed status
        return BidsFood.areEqual(inputFoodCooked, itemStackCooked);
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public FluidStack getCraftingResult() {
        return output.copy();
    }

}
