package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipeInputTemplate {

    private final FluidStack inputFluidStack;
    private final FluidStack secondaryInputFluidStack;
    private final ItemStack inputItemStack;
    private final CookingAccessory accessory;
    private final CookingLidUsage lidUsage;
    private final CookingHeatLevel minHeatLevel;
    private final CookingHeatLevel maxHeatLevel;

    public CookingRecipeInputTemplate(FluidStack inputFluidStack, FluidStack secondaryInputFluidStack, ItemStack inputItemStack, CookingAccessory accessory, CookingLidUsage lidUsage, CookingHeatLevel minHeatLevel, CookingHeatLevel maxHeatLevel) {
        this.inputFluidStack = inputFluidStack;
        this.secondaryInputFluidStack = secondaryInputFluidStack;
        this.inputItemStack = inputItemStack;
        this.accessory = accessory;
        this.lidUsage = lidUsage;
        this.minHeatLevel = minHeatLevel;
        this.maxHeatLevel = maxHeatLevel;
    }

    public FluidStack getInputFluidStack() {
        return inputFluidStack;
    }

    public FluidStack getSecondaryInputFluidStack() {
        return secondaryInputFluidStack;
    }

    public ItemStack getInputItemStack() {
        return inputItemStack;
    }

    public CookingAccessory getAccessory() {
        return accessory;
    }

    public CookingLidUsage getLidUsage() {
        return lidUsage;
    }

    public CookingHeatLevel getMinHeatLevel() {
        return minHeatLevel;
    }

    public CookingHeatLevel getMaxHeatLevel() {
        return maxHeatLevel;
    }

}
