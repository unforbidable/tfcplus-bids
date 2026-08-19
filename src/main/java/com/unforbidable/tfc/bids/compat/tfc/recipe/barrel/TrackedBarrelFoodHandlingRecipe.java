package com.unforbidable.tfc.bids.compat.tfc.recipe.barrel;

import com.unforbidable.tfc.bids.compat.tfc.recipe.RecipeTrackingHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import java.util.Stack;

public class TrackedBarrelFoodHandlingRecipe extends BarrelFoodHandlingRecipe {

    public TrackedBarrelFoodHandlingRecipe(ItemStack inputItem, FluidStack inputFluid, ItemStack outIS, FluidStack outputFluid) {
        super(inputItem, inputFluid, outIS, outputFluid);
    }

    @Override
    public Stack<ItemStack> getResult(ItemStack inIS, FluidStack inFS, int sealedTime) {
        Stack<ItemStack> stacks = super.getResult(inIS, inFS, sealedTime);

        for (ItemStack result : stacks) {
            RecipeTrackingHelper.onBarrelRecipeItemCrafted(result, this, inIS, inFS);
        }

        return stacks;
    }

}
