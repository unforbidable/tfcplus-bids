package com.unforbidable.tfc.bids.NEI;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class NeiHelper {

    public static boolean isFluidEqual(FluidStack fluidStack, ItemStack itemStack) {
        if (fluidStack == null) {
            return false;
        }

        // Try to reconstruct fluid stack from sponge block
        if (Block.getBlockFromItem(itemStack.getItem()) == Blocks.sponge && itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("FLUID")) {
            String fluidName = itemStack.getTagCompound().getString("FLUID");
            FluidStack other = FluidRegistry.getFluidStack(fluidName, itemStack.stackSize * FluidContainerRegistry.BUCKET_VOLUME);
            return other != null && other.isFluidEqual(fluidStack);
        }

        // Try to match fluid with registered containers
        return fluidStack.isFluidEqual(itemStack);
    }

}
