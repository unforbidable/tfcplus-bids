package com.unforbidable.tfc.bids.common.item;

import com.unforbidable.tfc.bids.core.drink.FluidHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDrinkingPottery extends ItemCommonPottery {

    public ItemDrinkingPottery() {
        super(true);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity) {
        return FluidHelper.fillContainerFromWorld(is, world, entity);
    }

}
