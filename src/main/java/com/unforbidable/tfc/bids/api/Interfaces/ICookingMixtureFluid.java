package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICookingMixtureFluid {

    void onCookingFluidPlaced(FluidStack cookingFluid);
    void onCookingFluidCombined(FluidStack cookingFluid, FluidStack original, FluidStack fluidStack);
    void onCookingFluidMerged(FluidStack cookingFluid, FluidStack originalFluid, FluidStack addedFluid);
    void onCookingFluidCooked(FluidStack cookingFluid, FluidStack originalFluid);
    boolean isValidCookedMealContainer(FluidStack cookingFluid, ItemStack container);
    ItemStack retrieveCookedMeal(FluidStack cookingFluid, ItemStack container, EntityPlayer player, boolean consumeFluid);
    boolean areCookingFluidsEqual(FluidStack cookingFluid, FluidStack anotherCookingFluid);

}
