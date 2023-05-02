package com.unforbidable.tfc.bids.Core.Drinks;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FluidHelper {

    public static void registerPartialFluidContainer(Fluid fluid, Item emptyContainerItem, int emptyDmg, Item filledContainerItem, int sip, int volume) {
        int count = volume / sip;

        FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, sip),
            new ItemStack(filledContainerItem, 1, count - 1), new ItemStack(emptyContainerItem, 1, emptyDmg));

        for (int i = 0; i < count - 1; i++) {
            FluidContainerRegistry.registerFluidContainer(new FluidStack(fluid, sip),
                new ItemStack(filledContainerItem, 1, i), new ItemStack(filledContainerItem, 1, i + 1));
        }
    }

}
