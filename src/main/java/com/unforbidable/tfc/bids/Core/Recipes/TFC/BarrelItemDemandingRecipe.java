package com.unforbidable.tfc.bids.Core.Recipes.TFC;

import com.dunk.tfc.TileEntities.TEBarrel;
import com.dunk.tfc.api.Crafting.BarrelRecipe;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class BarrelItemDemandingRecipe extends BarrelRecipe {

    public BarrelItemDemandingRecipe(ItemStack inputItem, FluidStack inputFluid,
            ItemStack outIS, FluidStack outputFluid) {
        super(inputItem, inputFluid, outIS, outputFluid);
    }

    @Override
    public Boolean matches(ItemStack item, FluidStack fluid, TEBarrel te) {
        // The super marches requires that there is no more
        // than the required amount of items
        // and here we demand that there is no less
        // which means the exact amount is required
        return super.matches(item, fluid, te) && hasEnoughItems(item, fluid, te);
    }

    public boolean hasEnoughItems(ItemStack item, FluidStack fluid, TEBarrel te) {
        return item.stackSize >= (int) Math.ceil(fluid.amount / recipeFluid.amount);
    }

}
