package com.unforbidable.tfc.bids.api.Crafting;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import com.unforbidable.tfc.bids.Core.Cooking.CookingHelper;
import com.unforbidable.tfc.bids.api.BidsFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class ScrewPressRecipe {

    final FluidStack outputFluid;
    final ItemStack inputItem;
    final ItemStack outputItem;
    final float resistance;

    public ScrewPressRecipe(FluidStack outputFluid, ItemStack inputItem, float resistance) {
        this(outputFluid, null, inputItem, resistance);
    }

    public ScrewPressRecipe(FluidStack outputFluid, ItemStack outputItem, ItemStack inputItem, float resistance) {
        this.outputFluid = outputFluid;
        this.outputItem = outputItem;
        this.inputItem = inputItem;
        this.resistance = resistance;
    }

    public boolean matches(ItemStack itemStack) {
        return itemStack.getItem() == inputItem.getItem()
                && itemStack.getItemDamage() == inputItem.getItemDamage()
                && (!(inputItem.getItem() instanceof IFood)
                        || foodMatches(itemStack));
    }

    private boolean foodMatches(ItemStack itemStack) {
        ItemStack inputItemStack = ItemFoodTFC.createTag(inputItem.copy());
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
        int inputCookedLevel = CookingHelper.getItemStackCookedLevel(inputItem);
        int ingredientCookedLevel = CookingHelper.getItemStackCookedLevel(itemStack);

        // If the recipe input item is cooked
        // the cooked level needs to match
        return inputCookedLevel == 0 || ingredientCookedLevel >= inputCookedLevel;
    }

    public ItemStack getInput() {
        return inputItem.copy();
    }

    public FluidStack getFluidCraftingResult() {
        return outputFluid.copy();
    }

    public ItemStack getItemCraftingResult() {
        return outputItem.copy();
    }

    public float getResistance() {
        return resistance;
    }

}
