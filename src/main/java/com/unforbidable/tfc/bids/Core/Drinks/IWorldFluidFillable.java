package com.unforbidable.tfc.bids.Core.Drinks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public interface IWorldFluidFillable {

    ItemStack getWorldFluidFilledItem(ItemStack is, World world, EntityPlayer player, Fluid fluid);

}
