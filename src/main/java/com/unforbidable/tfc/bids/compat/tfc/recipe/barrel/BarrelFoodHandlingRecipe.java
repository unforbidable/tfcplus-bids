package com.unforbidable.tfc.bids.compat.tfc.recipe.barrel;


import com.dunk.tfc.api.Crafting.BarrelRecipe;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import java.util.Stack;

public class BarrelFoodHandlingRecipe extends BarrelRecipe {

    public BarrelFoodHandlingRecipe(ItemStack inputItem, FluidStack inputFluid, ItemStack outIS, FluidStack outputFluid) {
        super(inputItem, inputFluid, outIS, outputFluid);
    }

    @Override
    public Stack<ItemStack> getResult(ItemStack inIS, FluidStack inFS, int sealedTime) {
        Stack<ItemStack> stacks = super.getResult(inIS, inFS, sealedTime);

        if (inIS != null && inIS.getItem() instanceof IFood && recipeOutIS != null && recipeOutIS.getItem() instanceof IFood) {
            // Handle food properly

            ItemStack result = recipeOutIS.copy();
            Food.setWeight(result, Food.getWeight(inIS));
            Food.setDecay(result, Food.getDecay(inIS));
            Food.setDecayTimer(result, Food.getDecayTimer(inIS));

            stacks.clear();
            stacks.push(result);
        }

        return stacks;
    }

}
