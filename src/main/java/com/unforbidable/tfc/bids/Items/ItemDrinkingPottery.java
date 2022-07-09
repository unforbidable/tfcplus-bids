package com.unforbidable.tfc.bids.Items;

import com.unforbidable.tfc.bids.Core.Drinks.DrinkHelper;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemDrinkingPottery extends ItemGenericPottery {

    public ItemDrinkingPottery() {
        super(true);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer entity) {
        return DrinkHelper.getFreshWaterDrink(is, world, entity,
                getMovingObjectPositionFromPlayer(world, entity, true));
    }

}
