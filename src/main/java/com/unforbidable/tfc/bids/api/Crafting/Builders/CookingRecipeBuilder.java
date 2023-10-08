package com.unforbidable.tfc.bids.api.Crafting.Builders;

import com.dunk.tfc.Core.TFC_Time;
import com.unforbidable.tfc.bids.api.Crafting.CookingRecipe;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingAccessory;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingHeatLevel;
import com.unforbidable.tfc.bids.api.Enums.EnumCookingLidUsage;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class CookingRecipeBuilder {

    protected FluidStack inputFluidStack;
    protected FluidStack secondaryInputFluidStack;
    protected FluidStack outputFluidStack;
    protected FluidStack secondaryOutputFluidStack;
    protected ItemStack inputItemStack;
    protected ItemStack outputItemStack;
    protected EnumCookingAccessory accessory = EnumCookingAccessory.NONE;
    protected EnumCookingLidUsage lidUsage;
    protected EnumCookingHeatLevel minHeatLevel;
    protected EnumCookingHeatLevel maxHeatLevel;
    protected long time;
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

    public CookingRecipeBuilder consumes(FluidStack inputFluidStack, ItemStack inputItemStack) {
        this.inputFluidStack = inputFluidStack;
        this.inputItemStack = inputItemStack;
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
        this.accessory = EnumCookingAccessory.STEAMING_MESH;
        return this;
    }

    public CookingRecipeBuilder withLid() {
        this.lidUsage = EnumCookingLidUsage.ON;
        return this;
    }

    public CookingRecipeBuilder withoutLid() {
        this.lidUsage = EnumCookingLidUsage.OFF;
        return this;
    }

    public CookingRecipeBuilder withHeat() {
        this.minHeatLevel = EnumCookingHeatLevel.LOW;
        this.maxHeatLevel = EnumCookingHeatLevel.HIGH;
        return this;
    }

    public CookingRecipeBuilder withHeat(EnumCookingHeatLevel heatLevel) {
        this.minHeatLevel = heatLevel;
        this.maxHeatLevel = heatLevel;
        return this;
    }

    public CookingRecipeBuilder withHeat(EnumCookingHeatLevel minHeatLevel, EnumCookingHeatLevel maxHeatLevel) {
        this.minHeatLevel = minHeatLevel;
        this.maxHeatLevel = maxHeatLevel;
        return this;
    }

    public CookingRecipeBuilder withoutHeat() {
        this.minHeatLevel = EnumCookingHeatLevel.NONE;
        this.maxHeatLevel = EnumCookingHeatLevel.NONE;
        return this;
    }

    public CookingRecipeBuilder inTime(long timeTicks) {
        this.time = timeTicks;
        return this;
    }

    public CookingRecipeBuilder inFixedTime(long timeTicks) {
        this.time = timeTicks;
        this.fixedTime = true;
        return this;
    }

    public CookingRecipe build() {
        return new CookingRecipe(inputFluidStack, secondaryInputFluidStack,
            outputFluidStack, secondaryOutputFluidStack,
            inputItemStack, outputItemStack,
            accessory, lidUsage, minHeatLevel, maxHeatLevel, time, fixedTime);

    }

}
