package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface ICookingMixtureItem {

    FluidStack getCookingMixFluid(ItemStack is);
    ItemStack getEmptyContainer(ItemStack is);

}
