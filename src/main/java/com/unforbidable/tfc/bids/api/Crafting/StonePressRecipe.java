package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
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
        ItemStack inputItemStack = ItemFoodTFC.createTag(input.copy());
        ItemStack ingredientItemStack = itemStack.copy();

        // Ignore infused status
        Food.setInfusion(ingredientItemStack, "dummy");
        Food.setInfusion(inputItemStack, "dummy");

        // Ignore also the cooked status
        Food.setCooked(ingredientItemStack,0);
        Food.setCooked(inputItemStack,0);

        // Use BidsFood to check boiled and steamed status
        return BidsFood.areEqual(inputItemStack, ingredientItemStack) &&
            foodCookedLevelMatches(itemStack);
    }

    private boolean foodCookedLevelMatches(ItemStack itemStack) {
        int inputCookedLevel = CookingHelper.getItemStackCookedLevel(input);
        int ingredientCookedLevel = CookingHelper.getItemStackCookedLevel(itemStack);

        // If the recipe input item is cooked
        // the cooked level needs to match
        return inputCookedLevel == 0 || ingredientCookedLevel >= inputCookedLevel;
    }

    public ItemStack getInput() {
        return input.copy();
    }

    public FluidStack getCraftingResult() {
        return output.copy();
    }

}
