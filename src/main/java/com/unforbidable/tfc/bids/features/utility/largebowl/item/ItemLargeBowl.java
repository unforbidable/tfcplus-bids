package com.unforbidable.tfc.bids.features.utility.largebowl.item;

import com.unforbidable.tfc.bids.BidsCreativeTabs;
import com.unforbidable.tfc.bids.common.item.ItemCommonPottery;
import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemLargeBowl extends ItemCommonPottery {

    public ItemLargeBowl() {
        super(true);
        setCreativeTab(BidsCreativeTabs.bidsTools);
        setMaxStackSize(16);
    }

    @Override
    public int getItemStackLimit(ItemStack is) {
        return 16;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player) {
        return FluidHelper.fillContainerFromWorld(is, world, player);
    }

}
