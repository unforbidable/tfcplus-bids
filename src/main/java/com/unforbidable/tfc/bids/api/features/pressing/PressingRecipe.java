package com.unforbidable.tfc.bids.api.features.pressing;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class PressingRecipe {

    private final ItemStack inputItem;
    private final ItemStack outputItem;
    private final FluidStack outputFluid;
    private final float resistance;

    public PressingRecipe(ItemStack inputItem, ItemStack outputItem, FluidStack outputFluid, float resistance) {
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.outputFluid = outputFluid;
        this.resistance = resistance;
    }

    public PressingRecipe(ItemStack inputItem, FluidStack outputFluid, float resistance) {
        this(inputItem, null, outputFluid, resistance);
    }

    public PressingRecipe(ItemStack inputItem, ItemStack outputItem, float resistance) {
        this(inputItem, outputItem, null, resistance);
    }

    public ItemStack getInputItem() {
        return inputItem;
    }

    public ItemStack getOutputItem() {
        return outputItem;
    }

    public FluidStack getOutputFluid() {
        return outputFluid;
    }

    public float getResistance() {
        return resistance;
    }

}
