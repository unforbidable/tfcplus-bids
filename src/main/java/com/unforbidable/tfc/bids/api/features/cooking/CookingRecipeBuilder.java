package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipeBuilder {

    protected FluidStack inputFluidStack;
    protected FluidStack secondaryInputFluidStack;
    protected FluidStack outputFluidStack;
    protected FluidStack secondaryOutputFluidStack;
    protected ItemStack inputItemStack;
    protected String inputOreName;
    protected ItemStack outputItemStack;
    protected CookingAccessory accessory = CookingAccessory.NONE;
    protected CookingLidUsage lidUsage;
    protected CookingHeatLevel minHeatLevel;
    protected CookingHeatLevel maxHeatLevel;
    protected float time;
    protected boolean fixedTime;

    public CookingRecipeBuilder consumes(FluidStack inputFluidStack) {
        this.inputFluidStack = inputFluidStack;
        return this;
    }

    public CookingRecipeBuilder consumes(FluidStack inputFluidStack, FluidStack secondaryInputFluidStack) {
        this.inputFluidStack = inputFluidStack;
        this.secondaryInputFluidStack = secondaryInputFluidStack;
        return this;
    }

    public CookingRecipeBuilder consumes(ItemStack inputItemStack) {
        this.inputItemStack = inputItemStack;
        return this;
    }

    public CookingRecipeBuilder consumes(String inputOreName) {
        this.inputOreName = inputOreName;
        return this;
    }

    public CookingRecipeBuilder consumes(FluidStack inputFluidStack, ItemStack inputItemStack) {
        this.inputFluidStack = inputFluidStack;
        this.inputItemStack = inputItemStack;
        return this;
    }

    public CookingRecipeBuilder consumes(FluidStack inputFluidStack, String inputOreName) {
        this.inputFluidStack = inputFluidStack;
        this.inputOreName = inputOreName;
        return this;
    }

    public CookingRecipeBuilder produces(FluidStack outputFluidStack) {
        this.outputFluidStack = outputFluidStack;
        return this;
    }

    public CookingRecipeBuilder produces(FluidStack outputFluidStack, FluidStack secondaryOutputFluidStack) {
        this.outputFluidStack = outputFluidStack;
        this.secondaryOutputFluidStack = secondaryOutputFluidStack;
        return this;
    }

    public CookingRecipeBuilder produces(FluidStack outputFluidStack, ItemStack outputItemStack) {
        this.outputFluidStack = outputFluidStack;
        this.outputItemStack = outputItemStack;
        return this;
    }

    public CookingRecipeBuilder produces(ItemStack outputItemStack) {
        this.outputItemStack = outputItemStack;
        return this;
    }

    public CookingRecipeBuilder withSteamingMesh() {
        this.accessory = CookingAccessory.STEAMING_MESH;
        return this;
    }

    public CookingRecipeBuilder withLid() {
        this.lidUsage = CookingLidUsage.ON;
        return this;
    }

    public CookingRecipeBuilder withoutLid() {
        this.lidUsage = CookingLidUsage.OFF;
        return this;
    }

    public CookingRecipeBuilder withHeat() {
        this.minHeatLevel = CookingHeatLevel.LOW;
        this.maxHeatLevel = CookingHeatLevel.HIGH;
        return this;
    }

    public CookingRecipeBuilder withHeat(CookingHeatLevel heatLevel) {
        this.minHeatLevel = heatLevel;
        this.maxHeatLevel = heatLevel;
        return this;
    }

    public CookingRecipeBuilder withHeat(CookingHeatLevel minHeatLevel, CookingHeatLevel maxHeatLevel) {
        this.minHeatLevel = minHeatLevel;
        this.maxHeatLevel = maxHeatLevel;
        return this;
    }

    public CookingRecipeBuilder withoutHeat() {
        this.minHeatLevel = CookingHeatLevel.NONE;
        this.maxHeatLevel = CookingHeatLevel.NONE;
        return this;
    }

    public CookingRecipeBuilder inTime(float timeTicks) {
        this.time = timeTicks;
        return this;
    }

    public CookingRecipeBuilder inFixedTime(float timeTicks) {
        this.time = timeTicks;
        this.fixedTime = true;
        return this;
    }

    public CookingRecipe build() {
        if (inputOreName != null) {
            return new CookingOreRecipe(inputFluidStack, secondaryInputFluidStack,
                outputFluidStack, secondaryOutputFluidStack,
                inputOreName, outputItemStack,
                accessory, lidUsage, minHeatLevel, maxHeatLevel, time, fixedTime);
        } else {
            return new CookingRecipe(inputFluidStack, secondaryInputFluidStack,
                outputFluidStack, secondaryOutputFluidStack,
                inputItemStack, outputItemStack,
                accessory, lidUsage, minHeatLevel, maxHeatLevel, time, fixedTime);
        }
    }

}
