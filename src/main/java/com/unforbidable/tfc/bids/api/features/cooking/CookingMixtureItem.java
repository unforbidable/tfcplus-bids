package com.unforbidable.tfc.bids.api.features.cooking;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public interface CookingMixtureItem {

    FluidStack getCookingFluid(ItemStack is);
    ItemStack getEmptyContainer(ItemStack is);

}
