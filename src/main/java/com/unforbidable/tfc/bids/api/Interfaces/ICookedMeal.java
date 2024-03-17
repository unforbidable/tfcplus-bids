package com.unforbidable.tfc.bids.api.Interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface ICookedMeal {

    void onCookedMealCreated(ItemStack is, EntityPlayer player);

}
