package com.unforbidable.tfc.bids.api.util.fluid;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class DualFluid {

    private final FluidStack fluidStack;

    public DualFluid(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    public static DualFluid of(FluidStack fs) {
        return new DualFluid(fs);
    }

    public static FluidStack create(Fluid primary, int primaryAmount, Fluid secondary, int secondaryAmount) {
        FluidStack fs = new FluidStack(primary, primaryAmount);
        fs.tag = new NBTTagCompound();

        return DualFluid.of(fs)
            .setSecondaryFluid(new FluidStack(secondary, secondaryAmount))
            .stack();
    }

    public DualFluid setSecondaryFluid(FluidStack fs) {
        if (fluidStack.tag == null) {
            fluidStack.tag = new NBTTagCompound();
        }

        NBTTagCompound secondaryTag = new NBTTagCompound();
        fs.writeToNBT(secondaryTag);
        fluidStack.tag.setTag("secondaryFluid", secondaryTag);

        return this;
    }

    public static boolean isDualFluidStack(FluidStack fs) {
        return fs.tag != null && fs.tag.hasKey("secondaryFluid");
    }

    public FluidStack stack() {
        return fluidStack;
    }

    public FluidStack getSecondaryFluid() {
        return FluidStack.loadFluidStackFromNBT(fluidStack.tag.getCompoundTag("secondaryFluid"));
    }

    public FluidStack getPrimaryFluid() {
        FluidStack secondary = getSecondaryFluid();
        return new FluidStack(fluidStack.getFluid(), fluidStack.amount - secondary.amount);
    }

    public boolean isEqual(FluidStack fs) {
        return isPrimaryFluidEqual(fs) || isSecondaryFluidEqual(fs);
    }

    public boolean isPrimaryFluidEqual(FluidStack fs) {
        return fluidStack.getFluid() == fs.getFluid();
    }

    public boolean isSecondaryFluidEqual(FluidStack fs) {
        FluidStack secondary = getSecondaryFluid();
        return secondary.getFluid() == fs.getFluid();
    }

}
