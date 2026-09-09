package com.unforbidable.tfc.bids.common.fluid;

import com.dunk.tfc.api.TFCFluids;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class FluidDilutable extends FluidCommon {

    public final Fluid dilutingFluid;
    public final int minDilution;

    public FluidDilutable(String fluidName) {
        this(fluidName, TFCFluids.FRESHWATER, 1000);
    }

    public FluidDilutable(String fluidName, Fluid dilutingFluid, int minDilution) {
        super(fluidName);
        this.dilutingFluid = dilutingFluid;
        this.minDilution = minDilution;
    }

    @Override
    public String getLocalizedName(FluidStack stack) {
        if (stack.amount > 0) {
            int dilutingAmount = getDilutingAmount(stack);
            if (dilutingAmount > 0) {
                int percent = Math.round(dilutingAmount * 100f / stack.amount);
                return super.getLocalizedName(stack) + " [" + percent + "%]";
            }
        }

        return super.getLocalizedName(stack);
    }

    public void setDilutingAmount(FluidStack fs, int amount) {
        if (amount == 0) {
            if (fs.tag != null) {
                fs.tag.removeTag("dilutingAmount");
                if (fs.tag.hasNoTags()) {
                    fs.tag = null;
                }
            }
        } else {
            if (fs.tag == null) {
                fs.tag = new NBTTagCompound();
            }

            fs.tag.setInteger("dilutingAmount", amount);
        }
    }

    public int getDilutingAmount(FluidStack fs) {
        if (fs.tag != null && fs.tag.hasKey("dilutingAmount")) {
            return fs.tag.getInteger("dilutingAmount");
        }

        return 0;
    }

    public FluidStack getDilutingFluid(FluidStack fs) {
        int dilutingAmount = getDilutingAmount(fs);
        if (dilutingAmount > 0) {
            return new FluidStack(dilutingFluid, dilutingAmount);
        }

        return null;
    }

    public boolean hasDilutingFluid(FluidStack fs) {
        return fs.tag != null && fs.tag.hasKey("dilutingAmount");
    }

    public int getUndilutedAmount(FluidStack fs) {
        return fs.amount - getDilutingAmount(fs);
    }

}
