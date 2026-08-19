package com.unforbidable.tfc.bids.api.features.soaking;

import cpw.mods.fml.common.eventhandler.Event;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public abstract class SoakingEvent extends Event {

    public ItemStack input;
    public ItemStack result;
    public FluidStack fluid;

    public SoakingEvent(ItemStack input, ItemStack result, FluidStack fluid) {
        this.input = input;
        this.result = result;
        this.fluid = fluid;
    }

    public static class ItemCrafted extends SoakingEvent {

        public ItemCrafted(ItemStack input, ItemStack result, FluidStack fluid) {
            super(input, result, fluid);
        }

    }

}
