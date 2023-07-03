package com.unforbidable.tfc.bids.Items;

import com.dunk.tfc.api.TFCFluids;
import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.Core.Drinks.FluidHelper;
import com.unforbidable.tfc.bids.Core.Drinks.IWorldFluidFillable;
import com.unforbidable.tfc.bids.api.BidsItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

public class ItemLargeBowl extends ItemGenericPottery implements IWorldFluidFillable {

    public ItemLargeBowl() {
        super(true);
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(4);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 4;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        return FluidHelper.fillContainerFromWorld(is, world, player);
    }

    @Override
    public ItemStack getWorldFluidFilledItem(ItemStack is, World world, EntityPlayer player, Fluid fluid) {
        if (fluid == TFCFluids.FRESHWATER) {
            return new ItemStack(BidsItems.freshWaterLargeBowl, 1, 0);
        }

        if (fluid == TFCFluids.SALTWATER) {
            return new ItemStack(BidsItems.saltWaterLargeBowl, 1, 0);
        }

        return null;
    }
}
